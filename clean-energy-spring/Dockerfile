# Use a base image with JDK (openjdk 17 is commonly used for Spring Boot)
FROM openjdk:17-jdk-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Install dependencies (skip tests for faster builds)
RUN ./mvnw dependency:go-offline

# Copy the rest of the application source code into the container
COPY src ./src

# Build the application (produce a .jar file)
RUN ./mvnw clean package -DskipTests

# Second stage: minimal image for running the app
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage (ensure the path matches the target folder)
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
