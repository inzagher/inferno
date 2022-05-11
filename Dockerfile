FROM openjdk:17-alpine
WORKDIR /opt/inferno
COPY ./target/inferno-1.0.0.jar inferno.jar
ENTRYPOINT ["java", "-jar", "inferno.jar"]