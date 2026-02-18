# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Root Parent POM'u kopyala ve install et (Burası hala zorunlu)
COPY pom.xml .
RUN mvn install -N -DskipTests

# (Common Library adımlarını çıkardık çünkü Eureka genelde buna ihtiyaç duymaz)

# 2. Discovery Server modülünü kopyala
# Klasör isminin 'discoveryServer' (veya senin projendeki adı neyse) olduğundan emin ol
COPY discoveryServer/pom.xml discoveryServer/
COPY discoveryServer/src discoveryServer/src

# 3. Build komutu
RUN mvn -f discoveryServer/pom.xml clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

# JAR dosyasını kopyala
COPY --from=build /app/discoveryServer/target/*.jar app.jar

# Eureka standart portu
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]