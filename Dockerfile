# Usa Java 17
FROM openjdk:17-jdk-slim

# Cria pasta da aplicação
WORKDIR /app

# Copia o jar gerado
COPY target/*.jar app.jar

# Porta padrão do Spring
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]