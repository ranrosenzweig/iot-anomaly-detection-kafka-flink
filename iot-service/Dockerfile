FROM openjdk:17-jdk-slim
MAINTAINER RanRosenzweig
WORKDIR /app
COPY target/iot-service-1.0-SNAPSHOT-jar-with-dependencies.jar /app/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "iot-service-1.0-SNAPSHOT-jar-with-dependencies.jar"]