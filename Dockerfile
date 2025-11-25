FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8082

CMD ["sh", "-c", "java $JAVA_OPTS -DLOG_LEVEL=$LOG_LEVEL -jar app.jar"]