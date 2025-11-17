 FROM gradle:8.5-jdk17 AS builder

 WORKDIR /app

 COPY . .

 RUN ./gradlew bootJar -x test --no-daemon

 # ---

 FROM amazoncorretto:17

 WORKDIR /app

 COPY --from=builder /app/build/libs/*.jar app.jar

 EXPOSE 8080

 ENTRYPOINT ["java", "-jar", "app.jar"]