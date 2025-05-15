FROM openjdk:17
COPY target/raiserbuddy.war /raiserbuddy.war
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "raiserbuddy.war"]
