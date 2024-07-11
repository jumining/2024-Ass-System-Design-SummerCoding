FROM gradle:8.8-jdk17 AS build
WORKDIR /home/app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build -x test

FROM openjdk:17-jdk-slim
COPY --from=build /home/app/build/libs/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]