FROM maven:3.8.2-jdk-8
ENV HOME=/home/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN ["mvn", "dependency:go-offline", "dependency:resolve"]
ADD . $HOME
ENTRYPOINT ["mvn", "spring-boot:run"]