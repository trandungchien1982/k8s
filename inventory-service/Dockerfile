FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml
COPY payment-service/pom.xml payment-service/pom.xml
COPY shipping-service/pom.xml shipping-service/pom.xml
COPY temporal-worker/pom.xml temporal-worker/pom.xml
COPY common/src common/src
COPY inventory-service/src inventory-service/src
RUN mvn -q -pl inventory-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/inventory-service/target/inventory-service-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
