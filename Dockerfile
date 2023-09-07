# Stage 1: Build the application in a Maven container
FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /app
COPY pom.xml ./
COPY src ./src/

# Build the application
RUN mvn clean package

# Stage 2: Use JDK 17 slim image as runtime
FROM openjdk:17-jdk-slim

# Copy the JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java","-jar","/app.jar"]