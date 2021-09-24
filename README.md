# game-of-three
When a player starts, it incepts a random (whole) number and sends it to the second player as an approach of starting the game. The receiving player can now always choose between adding one of {-1, 0, 1} to get to a number that is divisible by 3. Divide it by three. The resulting whole number is then sent back to the original sender.
## Tech

Social Hub Persistence Service uses a number of open source projects to work properly:

* [Docker] - makes it easier to create, deploy, and run applications by using containers.
* [Maven] - software project management and comprehension tool.
* [Spring boot] - java-based framework used to create a micro Service.
* [Apache Kafka] - an open-source distributed event streaming platform.

## Docker
### Build
```
docker-compose build
```
### Run
Note: The following command runs a single game instance. To test 2 players, run this command on different tabs in your terminal.
```
docker-compose run player
```
## Environment
* Docker version 20.10.7
* docker-compose version 1.28.6
* Ubuntu 20.04.2 LTS

[Docker]: <https://www.docker.com/>
[Maven]: <https://maven.apache.org/>
[Spring boot]: <https://spring.io/projects/spring-boot>
[Apache Kafka]: <https://kafka.apache.org/>
