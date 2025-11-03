# Stage 1: Build the application
FROM maven:3.9.11-amazoncorretto-25-al2023 AS builder
WORKDIR /app
COPY pom.xml .
# Cache dependencies
RUN mvn dependency:go-offline
COPY src ./src
# Build the JAR
RUN mvn clean package -DskipTests

# Stage 2: Run the application with debugging
FROM openjdk:26-ea-21-jdk-oraclelinux8
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]

