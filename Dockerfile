FROM gradle:9.5.1-jdk25 AS builder
WORKDIR /app

COPY . .

RUN ./gradlew bootJar -x test

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=builder /app/build/libs/document_service-*.jar app.jar

EXPOSE 5800

ENTRYPOINT ["java", "-jar", "app.jar"]