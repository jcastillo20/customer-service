# Customer Service API

Este proyecto es un microservicio desarrollado en Java con Spring Boot para gestionar la información de clientes. Permite registrar clientes, listar y consultar clientes por ID, y calcular métricas basadas en datos como la edad (promedio, desviación estándar, mediana, moda, percentiles y distribución etaria). La API está protegida mediante Spring Security; para acceder a los endpoints se requiere enviar un header `Authorization` con un token JWT Bearer.

---

## Tabla de Contenidos

- [Características](#características)
- [Funcionalidades](#funcionalidades)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos e Instalación](#requisitos-e-instalación)
- [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
- [Pruebas y Consumo de la API](#pruebas-y-consumo-de-la-api)
- [Monitoreo y Métricas](#monitoreo-y-métricas)
- [Decisiones Arquitectónicas y Patrones de Diseño](#decisiones-arquitectónicas-y-patrones-de-diseño)
- [Contacto](#contacto)

---

## Características

- **Seguridad:**  
  Endpoints protegidos mediante Spring Security. Se requiere el header `Authorization` con un token JWT de tipo Bearer válido.

- **Registro de Clientes:**  
  Permite crear un cliente nuevo; el proceso se instrumenta para medir el tiempo de ejecución, registrar contadores y almacenar logs.

- **Listado y Consulta:**  
  Permite obtener el listado completo de clientes y consultar datos específicos por ID, instrumentando la operación con métricas y logs.

- **Cálculo de Métricas:**  
  Realiza el cálculo de estadísticas (promedio de edad, desviación estándar, mediana, moda, percentiles y distribución etaria) a partir de la información de los clientes.

- **Monitoreo:**  
  Se utiliza Micrometer para instrumentar las operaciones y exponer métricas a través de Spring Boot Actuator.

---

## Funcionalidades

1. **Registrar Cliente**
  - **Endpoint:** `POST /api/customers`
  - **Descripción:** Registra un nuevo cliente a partir de un JSON enviado, y registra métricas (temporizador y contadores) junto con logs para seguimiento.

2. **Listar Clientes**
  - **Endpoint:** `GET /api/customers`
  - **Descripción:** Devuelve el listado de todos los clientes registrados, contando tiempos de ejecución y resultados en contadores y logs.

3. **Obtener Cliente por ID**
  - **Endpoint:** `GET /api/customers/{id}`
  - **Descripción:** Consulta un cliente individual mediante su ID. Se instrumenta el proceso con tiempos, contadores y logs.

4. **Calcular Métricas**
  - **Endpoint:** `GET /api/customers/metrics`
  - **Descripción:** Realiza el cálculo de estadísticas basadas en la edad de los clientes.
 

---

## Estructura del Proyecto

El proyecto se organiza en capas y directorios para facilitar el desarrollo y el mantenimiento:

- **Entidades (Domain):**
  - `Customer`
  - `CustomerRequestDto`
  - `CustomerResponseDto`
  - `MetricsDto`  
    *(Contiene las clases que representan el modelo de datos y las transferencias de información.)*

- **Application (appp):**
  - **resources:**
    - Controlador REST (por ejemplo, `CustomerController`) que expone los endpoints.
  - **mapper:**
    - Clases de mapeo (por ejemplo, `CustomerMapper`) que se encargan de las conversiones entre entidades, modelos de dominio y DTOs.
  - **service:**
    - Interfaces que definen la lógica de negocio.
    - **service/impl:**
      - Implementaciones de los servicios (por ejemplo, `CustomerServiceImpl`).

- **Infra:**
  - **adapter:**
    - Adaptadores que encapsulan la lógica de acceso a datos (por ejemplo, `CustomerRepositoryAdapter`), utilizando Spring Data JPA.

  - **Config:**
    - Configuración general de la aplicación, componentes y beans.

  - **handler.exception:**
    - Gestión centralizada de excepciones a través de clases y controladores de errores (por ejemplo, `CustomerServiceException`).

  - **Security:**
    - Configuración de Spring Security, validación de JWT y manejo del header `Authorization`.

---

## Requisitos e Instalación

### Requisitos

- **Java 17** o superior
- **Maven**

### Instalación

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/jcastillo20/customer-service.git
   cd customer-service
2. **Construir el proyecto:**

   ```bash
   mvn clean install
   
3. **Ejecución de la Aplicación**

   Para ejecutar la aplicación, utiliza el siguiente comando:
   ```bash
   mvn spring-boot:run
La aplicación se iniciará en el puerto 8080. Además, los endpoints de Spring Boot Actuator estarán disponibles para monitoreo, por ejemplo:

- http://localhost:8080/actuator/health

- http://localhost:8080/actuator/metrics

## Pruebas y Consumo de la API

A continuación se muestran ejemplos para probar los endpoints mediante cURL. Cada solicitud debe incluir el header Authorization con un token JWT Bearer válido.
1.  Ejemplo 1: Obtener Cliente por ID
    cURL:
    ```bash
    curl --location 'http://localhost:8080/api/customers/2' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c3VhcmlvMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NDY2ODQ2NDgsImV4cCI6MTc0ODk4ODI4OH0.nYw3S8QWLjDCqi43s0azDWjuxxsA8TaTsDNHTgNDoIaNLj3Iqzd0MzoecCqlAT3Q'

- Respuesta (JSON):
   ```json
    {
      "id": 2,
      "nombre": "Juan",
      "apellido": "Pérez",
      "edad": 30,
      "fechaNacimiento": "1995-01-15",
      "fechaFallecimientoEstimado": "2070-01-15"
    }
     
2. Ejemplo 2: Listar Clientes
   cURL:
    ```bash
    curl --location 'http://localhost:8080/api/customers' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c3VhcmlvMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NDY2ODQ2NDgsImV4cCI6MTc0ODk4ODI4OH0.nYw3S8QWLjDCqi43s0azDWjuxxsA8TaTsDNHTgNDoIaNLj3Iqzd0MzoecCqlAT3Q'
- Respuesta (JSON):
   ```json
       [
        {
          "id": 1,
          "nombre": "Ana",
          "apellido": "Gómez",
          "edad": 25,
          "fechaNacimiento": "2000-05-10",
          "fechaFallecimientoEstimado": "2075-05-10"
        },
        {
          "id": 2,
          "nombre": "Juan",
          "apellido": "Pérez",
          "edad": 30,
          "fechaNacimiento": "1995-01-15",
          "fechaFallecimientoEstimado": "2070-01-15"
        }
     ]

3. Ejemplo 3: Registrar un Cliente
cURL:
    ```bash
    curl --location 'http://localhost:8080/api/customers' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c3VhcmlvMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NDY2ODQ2NDgsImV4cCI6MTc0ODk4ODI4OH0.nYw3S8QWLjDCqi43s0azDWjuxxsA8TaTsDNHTgNDoIaNLj3Iqzd0MzoecCqlAT3Q' \
    --data-raw '{
    "nombre": "Carlos",
    "apellido": "Lopez",
    "edad": 40,
    "fechaNacimiento": "1985-03-20"
    }'

- Respuesta (JSON):
   ```json
       {
         "id": 3,
         "nombre": "Carlos",
         "apellido": "Lopez",
         "edad": 40,
         "fechaNacimiento": "1985-03-20",
         "fechaFallecimientoEstimado": "2060-03-20"
       }
4. Ejemplo 4: Consultar Métricas
cURL:
    ```bash
    curl --location 'http://localhost:8080/api/customers/metrics?=' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c3VhcmlvMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NDY2ODQ2NDgsImV4cCI6MTc0ODk4ODI4OH0.nYw3S8QWLjDCqi43s0azDWjuxxsA8TaTsDNHTgNDoIaNLj3Iqzd0MzoecCqlAT3Q'


- Respuesta (JSON):
   ```json
       {
        "promedioEdad": 35.0,
        "desviacionEstandar": 8.16496580927726,
        "totalClientes": 6,
        "edadMinima": 25,
        "edadMaxima": 50,
        "medianaEdad": 32.5,
        "modaEdad": 30.0,
        "percentilesEdad": {
            "p25": 30.0,
            "p50": 32.5,
            "p75": 38.75
        },
        "distribucionEtaria": [
            {
                "rango": "18-25",
                "porcentaje": 16.666666666666668
            },
            {
                "rango": "26-35",
                "porcentaje": 50.0
            },
            {
                "rango": "36-45",
                "porcentaje": 16.666666666666668
            },
            {
                "rango": "46-55",
                "porcentaje": 16.666666666666668
            },
            {
                "rango": "56-65",
                "porcentaje": 0.0
            }
        ]
  }

# Monitoreo y Métricas

La aplicación está instrumentada con Micrometer para registrar:

**Timers:**

- `customer.registration.timer`
- `customer.list.timer`
- `customer.get.timer`
- `customer.metrics.timer`

**Contadores:**  
Se registran los éxitos y errores en cada operación (por ejemplo, `customer.registration.success`, `customer.registration.error`, etc).

Estas métricas se exponen a través de Spring Boot Actuator y se pueden consultar en:

- [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)
- [http://localhost:8080/actuator/metrics/{nombreDeLaMetrica}](http://localhost:8080/actuator/metrics/%7BnombreDeLaMetrica%7D)

---

# Decisiones Arquitectónicas y Patrones de Diseño

## Arquitectura en Capas

- **Entidades (Domain):**  
  Se definen clases como `Customer`, `CustomerRequestDto`, `CustomerResponseDto` y `MetricsDto` para representar y transferir datos.

- **Application (appp):**
  - **resources:**  
    Controladores REST que exponen los endpoints.
  - **mapper:**  
    Clases encargadas de la conversión entre entidades, dominios y DTOs.
  - **service:**  
    Interfaz y su implementación (en `service/impl`) que contienen la lógica de negocio.

- **Infra:**  
  Contiene adaptadores para el acceso a la persistencia de datos (por ejemplo, `CustomerRepositoryAdapter`), aislando la lógica de datos de la lógica de negocio.

- **Config:**  
  Contiene configuraciones generales, definición de beans y otros componentes.

- **handler.exception:**  
  Maneja de manera centralizada las excepciones a través de clases especializadas (por ejemplo, `CustomerServiceException`).

- **Security:**  
  Configura Spring Security para validar tokens JWT y restringir el acceso a los endpoints mediante roles.

## Patrones de Diseño Aplicados

- **Mapper Pattern:**  
  Se utiliza para convertir entre entidades, modelos de dominio y DTOs (por ejemplo, `CustomerMapper`).

- **Repository Adapter:**  
  Aísla la lógica de acceso a los datos de la lógica de negocio, facilitando cambios en la implementación de persistencia.

- **Exception Handling Centralizado:**  
  Utiliza excepciones personalizadas para mantener respuestas consistentes ante errores.

- **Instrumentación y Logging:**  
  Se integra Micrometer para medir y reportar métricas, y SLF4J para el logging centralizado, facilitando el monitoreo y diagnóstico.

- **Seguridad Declarativa:**  
  Se emplea Spring Security con anotaciones como `@PreAuthorize` para restringir el acceso y validar la autenticación a través de tokens JWT.

---

# Contacto

Para consultas o colaboraciones, puedes contactar a:

- **Email:** juancrcastillo20@gmail.com
- **Nombre y Apellidos:** Juan Castillo Rodriguez

