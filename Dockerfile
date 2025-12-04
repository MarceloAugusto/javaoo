FROM openjdk:17-jdk

# Configurar o diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR para dentro do contêiner
COPY target/myapp.jar app.jar

# Expôr a porta 8080
EXPOSE 8080

# Definir o comando para rodar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
