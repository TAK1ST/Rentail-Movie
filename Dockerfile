# Use official JDK 23 base image
FROM openjdk:23-slim

# Set working directory
WORKDIR /app

# Install Ant
RUN apt-get update && \
    apt-get install -y ant && \
    apt-get clean

# Copy the project files
COPY . .

# Copy the build.xml file
COPY build.xml .

# Build the project using Ant
RUN ant clean compile

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
CMD ["ant", "run"]