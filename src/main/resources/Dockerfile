# Usa una imagen base de OpenJDK 22 o Amazon Corretto 22
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR generado (asegúrate de que se construya con mvn clean package)
COPY target/customer-service-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de la aplicación (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
