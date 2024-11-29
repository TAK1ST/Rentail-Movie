FROM openjdk:23 as build

RUN apt-get update && apt-get install -y ant

WORKDIR /app

COPY . /app

RUN ant clean jar  

FROM openjdk:23

WORKDIR /app

COPY --from=build /app/dist/Rental-Movie.jar /app/Rental-Movie.jar

EXPOSE 8080

CMD ["java", "-jar", "Rental-Movie.jar"]

