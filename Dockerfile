# Jar 파일 빌드
FROM eclipse-temurin:17 as builder

# 소스 코드와 Gradle Wrapper 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle Wrapper 실행 권한 부여 및 애플리케이션 빌드
RUN chmod +x ./gradlew && ./gradlew bootJar

# Jar 실행
FROM eclipse-temurin:17-jre as runtime

# 사용자 및 그룹 생성
RUN addgroup --system --gid 1000 worker && \
    adduser --system --uid 1000 --ingroup worker --disabled-password worker

# 파일 저장을 위한 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/images && \
    chown worker:worker /app/images

# 빌드된 Jar 파일 복사
COPY --from=builder /build/libs/*.jar app.jar

# 사용자 전환
USER worker:worker

# 환경변수 설정
ENV PROFILE ${PROFILE}

# 포트 8080 열기
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "/app.jar"]
