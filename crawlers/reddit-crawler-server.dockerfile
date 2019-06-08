FROM openjdk:13-alpine

COPY reddit-crawler-server/target/reddit-crawler-server-0.0.1-SNAPSHOT.jar /usr/app/

ENTRYPOINT ["java","-jar","/usr/app/reddit-crawler-server-0.0.1-SNAPSHOT.jar"] 