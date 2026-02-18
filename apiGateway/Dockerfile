# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Root POM kopyalanır
COPY pom.xml .

# 2. ÖNEMLİ: Common Library'yi kopyala ve INSTALL et
# Diğer servisler bu JAR'a bağımlı olduğu için önce bu derlenmeli
COPY common-library/pom.xml common-library/
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml clean install -DskipTests

# 3. Şimdi apiGateway modülünü kopyala
COPY apiGateway/pom.xml apiGateway/
COPY apiGateway/src apiGateway/src

# 4. Build komutu
# Common library artık yerel maven deposunda (container içinde) olduğu için hata vermez
RUN mvn -f apiGateway/pom.xml clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

# JAR dosyasını kopyala
COPY --from=build /app/apiGateway/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]