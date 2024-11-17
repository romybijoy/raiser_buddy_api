FROM openjdk:17
ADD target/raiserbuddy-0.0.1-SNAPSHOT.war raiserbuddy-0.0.1-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/raiserbuddy-0.0.1-SNAPSHOT.war"]
