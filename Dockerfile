FROM java:8

ADD target/toscaide-0.0.1-SNAPSHOT.jar /usr/src/myapp/toscaide-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/myapp/toscaide-0.0.1-SNAPSHOT.jar"]

CMD [""]
