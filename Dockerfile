# Docker 镜像构建
# 选择基础镜像
FROM maven:3.8.4-jdk-11-slim as builder

# 解决容器时间和真实时间相差 8 小时的问题
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone

# 复制代码到容器内
WORKDIR /app
COPY pom.xml .
COPY src ./src

# 打包构建
RUN mvn package -DskipTests

# 容器启动时运行 jar 包
CMD ["java","-jar","/app/target/yudada-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]