#! /bin/bash

mvn clean install

#SERVER_PORT=8081

java -jar ./target/toscaide-0.0.1-SNAPSHOT.jar
