FROM bellsoft/liberica-openjdk-alpine:25
RUN apk update && apk add --no-cache curl
COPY target/app.jar /app.jar
EXPOSE 7070
CMD ["java", "-jar", "/app.jar"]