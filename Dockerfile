# Etapa de build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do código e builda
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de execução com imagem enxuta
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Evita execução como root (boa prática de segurança)
RUN adduser -D springuser
USER springuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]