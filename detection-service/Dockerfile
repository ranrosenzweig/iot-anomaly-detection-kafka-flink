FROM openjdk:18-jdk-slim
MAINTAINER RanRosenzweig
WORKDIR /app
COPY target/detection-service-1.0-SNAPSHOT-jar-with-dependencies.jar /app/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "detection-service-1.0-SNAPSHOT-jar-with-dependencies.jar"]