FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn -pl :bootstrap -am clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]
