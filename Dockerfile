FROM openjdk:18.0.1

WORKDIR /app

COPY build/libs/project-module3-*.jar /app/project-module3.jar

ENTRYPOINT ["java", "-jar","/app/project-module3.jar"]