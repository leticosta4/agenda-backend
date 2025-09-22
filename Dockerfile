FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src



CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev", "-Dspring-boot.run.jvmArguments=-Dspring.devtools.restart.enabled=true -Dspring.devtools.livereload.enabled=true"]
EXPOSE 8080 
