FROM openjdk:17
WORKDIR /app
COPY target/ZenithActive-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]