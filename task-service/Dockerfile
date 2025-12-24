FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew || true
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]