# Etapa de construção - Build stage
FROM maven:3.8.6-openjdk-17 AS build

# Definir diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar todos os arquivos do projeto para o contêiner
COPY . .

# Rodar o Maven para construir o projeto
RUN mvn clean install

# Etapa de execução - Run stage
FROM openjdk:17-jdk-slim

# Expor a porta 8080
EXPOSE 8080

# Copiar o arquivo JAR gerado da etapa de build
COPY --from=build /app/target/deploy_render-1.0.0.jar /app/app.jar

# Definir o comando para rodar o JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
