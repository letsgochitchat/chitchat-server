FROM openjdk:17-jdk-slim

EXPOSE 8080
ENV TZ=Asia/Seoul
COPY ./build/libs/*.jar server.jar
ENTRYPOINT ["java","-jar","/server.jar"]
