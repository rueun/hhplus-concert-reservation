# 1. OpenJDK 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 애플리케이션 JAR 파일을 컨테이너에 복사
ARG JAR_FILE=build/libs/${JAR_FILE}
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# 3. 애플리케이션 실행
ENTRYPOINT ["java","-jar","/app.jar"]