FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle --stop
RUN rm -rf /home/gradle/.gradle/caches/
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

