FROM openjdk:8-jre-alpine

MAINTAINER vncetin@gmail.com

ENV VERTICLE_FILE vertx-maven-template-1.0.0-SNAPSHOT-fat.jar
ENV VERTICLE_HOME /opt/verx-maven-template

EXPOSE 8080:8080

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/
COPY src/main/conf/app.json $VERTICLE_HOME/


# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE -conf app.json"]
