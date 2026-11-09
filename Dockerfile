FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml lombok.config ./
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /opt/jirarush
COPY --from=build /build/target/jira-1.0.jar jira.jar
COPY resources ./resources
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "jira.jar"]
