# ---------- Build Stage ----------
FROM gradle:8.4-jdk17 AS build
WORKDIR /app

# Only copy build files first to optimize caching
COPY /build.gradle /settings.gradle /app/
COPY /gradle /app/gradle
COPY /gradlew /app/
COPY /gradlew.bat /app/

# Copy rest of the backend source
COPY /src /app/src

# Build JAR
RUN ./gradlew bootJar --no-daemon

# ---------- Run Stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
