FROM adoptopenjdk/openjdk15:ubi as build

#makedir app & cd app
# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline. 
# This is a separate step so the dependencies will be cached unless 
# the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Copy the production.properties file from your host machine into the container
#COPY src/main/resources/production.properties production.properties

# Package the application
RUN ./mvnw package -DskipTests
# RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


#### Stage 2: A minimal docker image with command to run the app 
FROM adoptopenjdk/openjdk15:ubi

# ARG DEPENDENCY=/app/target/dependency
ARG JAR_FILE=/app/target/*.jar
WORKDIR /app

# Copy project dependencies from the build stage
# COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
# COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build $JAR_FILE /app/runner.jar

# ENTRYPOINT [ "printenv" ]
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]
