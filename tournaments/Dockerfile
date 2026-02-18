# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Copy the Root Parent POM and install it to the local .m2 repository
# This prevents "parent not found" errors for the sub-modules
COPY pom.xml .
RUN mvn install -N -DskipTests

# 2. IMPORTANT: Copy and INSTALL the Common Library
# This must be built first as other services depend on this JAR
COPY common-library/pom.xml common-library/
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml clean install -DskipTests

# 3. Copy the tournaments module files
COPY tournaments/pom.xml tournaments/
COPY tournaments/src tournaments/src

# 4. Build command
# Common library is now in the local maven repository, so no errors
RUN mvn -f tournaments/pom.xml clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

# Copy the executable JAR file
COPY --from=build /app/tournaments/target/*.jar app.jar

EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]