# 构建阶段（使用Maven基础镜像）
FROM maven:3.8.6-eclipse-temurin-11-alpine AS build
WORKDIR /app

# 复制POM文件并下载依赖（利用Docker缓存层）
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# 复制源代码并构建
COPY src ./src
RUN mvn clean package assembly:single -DskipTests -B --no-transfer-progress

# 运行阶段（使用轻量级JRE镜像）
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app

# 从构建阶段复制JAR文件
COPY --from=build /app/target/*-jar-with-dependencies.jar /app/app.jar

# 暴露端口并设置启动命令
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
