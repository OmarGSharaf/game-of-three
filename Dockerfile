FROM maven:3.8.2-jdk-8
COPY . /player
WORKDIR /player
ENTRYPOINT ["mvn", "spring-boot:run"]