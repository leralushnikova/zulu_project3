FROM ubuntu:latest
#LABEL authors="ValeriyaLushnikova"

RUN apt-get update && apt-get install -y \\


COPY ./target/zulu_project3.jar ./zulu_project3.jar

ENV PORT=8080
EXPOSE 8080

# Команда для запуска приложения при старте контейнера
CMD ["zulu_project3", "-jar", "zulu_project3.jar"]