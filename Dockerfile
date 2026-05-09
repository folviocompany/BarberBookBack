# ── Estágio 1: Build ────────────────────────────────────────────────────────
FROM maven:3.9-amazoncorretto-21 AS build

WORKDIR /app

# Copia o POM e baixa as dependências primeiro — aproveita o cache do Docker
# enquanto o código-fonte não mudar
COPY demo/pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e gera o JAR
COPY demo/src ./src
RUN mvn clean package -DskipTests -B

# ── Estágio 2: Runtime ───────────────────────────────────────────────────────
FROM amazoncorretto:21-alpine

WORKDIR /app

# Usuário não-root para reduzir superfície de ataque
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia apenas o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Ajusta posse do arquivo para o usuário não-root
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar", \
  "--spring.profiles.active=prod"]
