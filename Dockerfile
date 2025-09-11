# ====== BUILD ======
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# cache das dependências
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# código-fonte
COPY src ./src

# build
RUN mvn -q -DskipTests clean package

# ====== RUNTIME ======
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"

# se no pom.xml tiver <finalName>app</finalName>, copie exatamente:
# COPY --from=build /app/target/app.jar app.jar
# caso contrário, pode manter o wildcard:
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
