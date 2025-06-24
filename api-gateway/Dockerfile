# =============================================================================
# Stage 1: Build Stage using Maven with JDK 21
# =============================================================================

# Use the Maven 3.9.9 image based on Eclipse Temurin 21.
# This image provides both Maven and JDK 21, ensuring that our build process
# uses a consistent environment.
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory for the build process to /app.
WORKDIR /app

# Copy the Maven project file (pom.xml) into the container.
# The pom.xml contains the project configuration and lists all dependencies.
COPY pom.xml .

# Pre-download all Maven dependencies using the "go-offline" mode.
# This caches the required dependencies, so that subsequent builds can run faster,
# even if the source code changes.
RUN mvn dependency:go-offline -B

# Copy the entire source code from the local "src" directory into the container.
# This makes your application's source code available for compilation.
COPY src ./src

# Clean any previous builds and package the application.
# This command compiles the source code and bundles the application into a single jar file.
RUN mvn clean package

# =============================================================================
# Stage 2: Runtime Stage using OpenJDK 21
# =============================================================================

# Use the OpenJDK 21 image to run the packaged application.
FROM openjdk:21-jdk AS runner

# Set the working directory for the runtime environment to /app.
WORKDIR /app

# Copy the generated jar file from the build stage into the runtime container.
# The jar file is located in the /app/target directory within the builder stage.
COPY --from=builder ./app/target/api-gateway-0.0.1-SNAPSHOT.jar ./app.jar

# Expose port 4004 so that the application can accept incoming network requests.
EXPOSE 4004

# Define the command to run when the container starts.
# Here, the Java command is used to execute the jar file.
ENTRYPOINT ["java", "-jar", "app.jar"]
