## vertx-maven-template
Sample template vert.x api project which fetches github job data from api and stores on mongdb 

## Tech/framework used
vert.x , mongoDB

<b>Built with</b>
- [Maven](https://maven.apache.org/)

## Installation

Maven have to be installed
MongoDB install on local (or dont if docker installed)


"maven install" for build
java -jar target/ MainVerticle-1.0.0-SNAPSHOT-fat.jar -conf src/conf/app.json

or 

docker-compose build
docker-compose up

## Testing
For both unit and integration test

mvn test -Dmaven.test.skip=false


© [Volkan Cetin]()
