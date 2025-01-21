FROM openjdk:18.0.1

WORKDIR /app

COPY build/libs/project-module3-1.0-SNAPSHOT.jar /project-module3.jar

ENTRYPOINT ["java", "-jar","/project-module3.jar"]