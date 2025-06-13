# ---------- Build Stage ----------
FROM gradle:8.4-jdk17 AS build
WORKDIR /app

# Only copy build files first to optimize caching
COPY backend/build.gradle.kts backend/settings.gradle.kts /app/
COPY backend/gradle /app/gradle
COPY backend/gradlew /app/
COPY backend/gradlew.bat /app/

# Copy rest of the backend source
COPY backend/src /app/src

# Build JAR
RUN ./gradlew bootJar --no-daemon

# ---------- Run Stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
