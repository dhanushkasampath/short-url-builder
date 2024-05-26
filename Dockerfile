FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/short-url-builder.jar short-url-builder.jar
ENTRYPOINT ["java", "-jar", "/short-url-builder.jar"]