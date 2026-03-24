
# Usamos o Maven com JDK 17 para gerar o .jar dentro da Render
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app


COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Usamos uma imagem leve apenas com o JRE para rodar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Porta
EXPOSE 8080

# Comando para iniciar a API
ENTRYPOINT ["java", "-jar", "app.jar"]