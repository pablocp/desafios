FROM openjdk:13-alpine

COPY telegram-bot/target/telegram-bot-0.0.1-SNAPSHOT.jar /usr/app/

ENTRYPOINT ["java","-jar","/usr/app/telegram-bot-0.0.1-SNAPSHOT.jar"]  
