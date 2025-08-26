FROM postgres:latest
RUN apt-get -y update && apt-get -y install curl && apt-get install -y unzip \
 && curl https://edu.postgrespro.ru/demo-small.zip --output demo.zip         \
 && unzip demo.zip && rm demo.zip && mv demo*.sql demo.sql                   \
 && sed -i 's/DROP DATABASE demo/drop database if exists demo/g' demo.sql    \
 && mv demo.sql /docker-entrypoint-initdb.d/
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=postgres
EXPOSE 5432