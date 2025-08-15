# 1. Java 런타임 환경 이미지
FROM openjdk:17-jdk-slim

# 2. 빌드된 JAR 복사 (하나만 있다고 가정)
COPY build/libs/*.jar app.jar

# 3. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]