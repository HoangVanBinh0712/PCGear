# Use a base image with Java installed
FROM openjdk:latest

# Set the working directory in the container
WORKDIR /app

# Copy the Jar file from your local machine to the container
COPY /target/*.jar /app

# Specify the command to run your Jar file
CMD ["java", "-jar", "gear-0.0.1-SNAPSHOT.jar"]
