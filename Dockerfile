FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/raiserbuddy.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]