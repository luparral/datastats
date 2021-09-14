FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=datastats-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]