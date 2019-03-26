# MerchFinder
Java EE demo project for interaction with the Ebay REST API

## Table of Contents
- [Objective](#objective)
- [What's included](#whats-included)
- [Documentation](#documentation)
- [Tools](#tools)
- [Resources](#resources)
- [Creators](#creators)

## Objective
The development of this web application demo was part of the Software Engineering course. It's an introduction to Java EE development and to the integration of different technologies (Docker, MySQL database and Kafka cluster) in the same project.

## What's included
```
KafkaConsumer/
    +-- src/main/java/com/ua/kafkaconsumer/
        +-- consumer.java
        +-- main.java
    +-- pom.xml
MerchFinder/
    +-- _dbqueries/
        +-- queries.sql
    +-- _dockerconfigs/
        +-- docker-compose.yml
    +-- src/main/
        +-- java/com/ua/merchfinder/
            +-- controllers/
                +-- GetEbaySearchResultsCtrl.java
            +-- entities/
                +-- Productssearches.java
            +-- kafka/
                +-- EventProducer.java
            +-- scheduler/
                +-- TimerSessionBean.java
            +-- session/
                +-- AbstractFacade.java
                +-- ProductssearchesFacade.java
        +-- resources/META-INF/
            +-- persistence.xml
        +-- webapp/
            +-- WEB-INF/
                +-- glassfish-resources.xml
                +-- web.xml
            +-- index.jsp
            +-- main.css
    +-- pom.xml
```

## Documentation

The documentation of this project can be found in the [wiki](https://github.com/Diogo525/MerchFinder/wiki).

## Tools

 - OS: Ubuntu 16.04

 - IDE: NetBeans v8.2

 - Web application server: Glassfish v4.1.1

 - JDK: OpenJDK v1.8.0_191

 - JavaEE: 7

 - Kafka docker image: spotify/kafka
 
 - MySQL docker image: mysql/mysql-server:5.7


## Resources

 - Run Kafka in Docker
      - https://gist.github.com/abacaphiliac/f0553548f9c577214d16290c2e751071
      
## Creators

**Diogo Guedes**

- <https://github.com/Diogo525>

 
