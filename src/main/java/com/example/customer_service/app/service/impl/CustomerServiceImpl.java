package com.example.customer_service.app.service.impl;

import com.example.customer_service.app.mapper.CustomerMapper;
import com.example.customer_service.app.service.CustomerService;
import com.example.customer_service.app.util.Constants;
import com.example.customer_service.domain.model.*;
import com.example.customer_service.infra.adapter.CustomerRepositoryAdapter;
import com.example.customer_service.infra.handle.exception.CustomerServiceException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepositoryAdapter repository;

    private final MeterRegistry meterRegistry;
    public CustomerServiceImpl(CustomerRepositoryAdapter repository, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public CustomerResponseDto registerCustomer(CustomerRequestDto customerRequestDto) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            // Conversion del DTO al dominio
            Customer customer = CustomerMapper.toDomain(customerRequestDto);
            // Calcula la fecha estimada de algún evento (por ejemplo, fecha de fallecimiento estimado)
            customer.setFechaEstimadoEvento(calcularFechaEstimadoEvento(customer.getFechaNacimiento()));
            // Persiste el cliente
            Customer saved = repository.save(customer);
            // Incrementa el contador de registros exitosos
            meterRegistry.counter("customer.registration.success").increment();
            log.info("Cliente registrado exitosamente: {} {}", saved.getNombre(), saved.getApellido());
            // Retorna el DTO de respuesta
            return CustomerMapper.toResponseDto(saved);
        } catch (Exception e) {
            // Incrementa el contador de errores
            meterRegistry.counter("customer.registration.error").increment();
            log.error("Error registrando el cliente: {}", e.getMessage(), e);
            throw new CustomerServiceException("Error registrando el cliente: " + e.getMessage(), e);
        } finally {
            // Registra el tiempo total empleado en métrica
            sample.stop(meterRegistry.timer("customer.registration.timer"));
        }
    }

    @Override
    public List<CustomerResponseDto> listCustomers() {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            List<Customer> customers = repository.findAll();
            List<CustomerResponseDto> response = customers.stream()
                    .map(CustomerMapper::toResponseDto)
                    .collect(Collectors.toList());

            // Incrementar contador de éxitos y escribir log informativo
            meterRegistry.counter("customer.list.success").increment();
            log.info("Se listaron {} clientes exitosamente.", customers.size());

            return response;
        } catch (Exception e) {
            // Incrementar contador de errores y registrar el error en el log
            meterRegistry.counter("customer.list.error").increment();
            log.error("Error listando los clientes: {}", e.getMessage(), e);
            throw new CustomerServiceException("Error listando los clientes: " + e.getMessage(), e);
        } finally {
            // Termina la medición y registra la métrica del tiempo de ejecución
            sample.stop(meterRegistry.timer("customer.list.timer"));
        }
    }

    @Override
    public MetricsDto getMetrics() {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            List<Customer> customers = repository.findAll();
            MetricsDto metrics = new MetricsDto();

            if (!customers.isEmpty()) {
                // Extraer edades y ordenar
                List<Integer> edades = customers.stream()
                        .map(Customer::getEdad)
                        .sorted()
                        .collect(Collectors.toList());
                int total = customers.size();

                // Calcular promedio
                double promedio = edades.stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0);
                // Calcular desviación estándar
                double variance = edades.stream()
                        .mapToDouble(e -> Math.pow(e - promedio, 2))
                        .average()
                        .orElse(0);
                double desviacion = Math.sqrt(variance);
                // Edad mínima y máxima
                int minEdad = edades.get(0);
                int maxEdad = edades.get(edades.size() - 1);
                // Mediana
                double mediana = calcularMediana(edades);
                // Moda
                double moda = calcularModa(edades);
                // Percentiles
                PercentilesDto percentiles = new PercentilesDto(
                        calcularPercentil(edades, 0.25),
                        calcularPercentil(edades, 0.50),
                        calcularPercentil(edades, 0.75)
                );
                // Distribución etaria
                List<RangoEtarioDto> distribucion = calcularDistribucion(edades, total);

                metrics.setPromedioEdad(promedio);
                metrics.setDesviacionEstandar(desviacion);
                metrics.setTotalClientes(total);
                metrics.setEdadMinima(minEdad);
                metrics.setEdadMaxima(maxEdad);
                metrics.setMedianaEdad(mediana);
                metrics.setModaEdad(moda);
                metrics.setPercentilesEdad(percentiles);
                metrics.setDistribucionEtaria(distribucion);
            }
            // Incrementa contador de éxitos
            meterRegistry.counter("customer.metrics.success").increment();
            log.info("Métricas calculadas exitosamente, total de clientes: {}", customers.size());
            return metrics;
        } catch (Exception e) {
            // Incrementa contador de errores
            meterRegistry.counter("customer.metrics.error").increment();
            log.error("Error calculando las métricas: {}", e.getMessage(), e);
            throw new CustomerServiceException("Error calculando las métricas: " + e.getMessage(), e);
        } finally {
            // Registra el tiempo total empleado en la métrica
            sample.stop(meterRegistry.timer("customer.metrics.timer"));
        }
    }

    private LocalDate calcularFechaEstimadoEvento(LocalDate fechaNacimiento) {
        return fechaNacimiento.plusYears(Constants.ESPERANZA_VIDA);
    }

    private double calcularDesviacionEstandar(List<Customer> customers, double media) {
        double variance = customers.stream()
                .mapToDouble(c -> Math.pow(c.getEdad() - media, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }

    private double calcularMediana(List<Integer> edades) {
        int n = edades.size();
        if (n % 2 == 0) {
            return (edades.get(n/2 - 1) + edades.get(n/2)) / 2.0;
        } else {
            return edades.get(n/2);
        }
    }

    private double calcularModa(List<Integer> edades) {
        java.util.Map<Integer, Long> frecuencias = edades.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        return frecuencias.entrySet().stream()
                .max(java.util.Comparator.comparingLong(java.util.Map.Entry::getValue))
                .map(java.util.Map.Entry::getKey)
                .orElse(0);
    }

    private double calcularPercentil(List<Integer> edades, double porcentaje) {
        int n = edades.size();
        if (n == 0) return 0;
        double pos = porcentaje * (n - 1);
        int indexInferior = (int) Math.floor(pos);
        int indexSuperior = (int) Math.ceil(pos);
        if (indexInferior == indexSuperior) {
            return edades.get(indexInferior);
        }
        double fraccion = pos - indexInferior;
        return edades.get(indexInferior) + fraccion * (edades.get(indexSuperior) - edades.get(indexInferior));
    }

    private List<RangoEtarioDto> calcularDistribucion(List<Integer> edades, int total) {
        int[] limites = {18, 26, 36, 46, 56, 66};
        java.util.List<RangoEtarioDto> distribucion = new java.util.ArrayList<>();

        for (int i = 0; i < limites.length - 1; i++) {
            int lower = limites[i];
            int upper = limites[i + 1] - 1;
            long count = edades.stream()
                    .filter(e -> e >= lower && e <= upper)
                    .count();
            double porcentaje = (total > 0) ? (count * 100.0 / total) : 0;
            distribucion.add(new RangoEtarioDto(lower + "-" + upper, porcentaje));
        }

        return distribucion;
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Customer customer = repository.findById(id)
                    .orElseThrow(() -> new CustomerServiceException("Cliente no encontrado con id: " + id));
            // Incrementa el contador de éxito
            meterRegistry.counter("customer.get.success").increment();
            log.info("Cliente con id {} obtenido exitosamente.", id);
            return CustomerMapper.toResponseDto(customer);
        } catch (Exception e) {
            // Incrementa el contador de error
            meterRegistry.counter("customer.get.error").increment();
            log.error("Error obteniendo el cliente con id {}: {}", id, e.getMessage(), e);
            throw new CustomerServiceException("Error obteniendo el cliente con id " + id + ": " + e.getMessage(), e);
        } finally {
            // Finaliza la medición del tiempo y registra la métrica
            sample.stop(meterRegistry.timer("customer.get.timer"));
        }
    }
}
