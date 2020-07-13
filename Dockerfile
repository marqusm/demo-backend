FROM openjdk:14.0-jdk-slim
WORKDIR /app

COPY build/libs/demo-backend-rest-java-springboot.jar /app/service.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/service.jar"]
