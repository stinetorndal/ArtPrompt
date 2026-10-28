FROM bellsoft/liberica-openjdk-alpine:25
RUN apk update && apk add --no-cache curl
COPY target/app.jar /app.jar
EXPOSE 7070
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -fs http://localhost:7070/health || exit 1
CMD ["java", "-jar", "/app.jar"]