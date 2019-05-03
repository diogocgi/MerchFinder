# MerchFinder
Java EE demo project and interaction with the Ebay REST API

## Table of Contents
- [Objective](#objective)
- [Summary](#summary)
- [Tools](#tools)
- [Resources](#resources)
- [Testing](#testing)
- [Creators](#creators)

## Objective
The development of this web application demo was part of the Software Engineering course. It's an introduction to Java EE development and to the integration of different technologies (Docker, MySQL database and Kafka cluster) in the same project.

## Summary

This project uses the Ebay Finding API to obtain search results and display them to the users. It has a database that serves as cache, which reduces the number of API calls and decreases the response time. The database stores and updates the product searches made by users and returns them when the same searches are made.

Kafka is used to store message updates of the products in the database. This messages are consumed by the kafka client program.

![demo_img]

[demo_img]: https://github.com/Diogo525/MerchFinder/blob/master/images/demo_1.png

For further details check the [wiki](https://github.com/Diogo525/MerchFinder/wiki).

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
 - Docker configuration
      - https://docs.docker.com/install/linux/docker-ce/ubuntu/
 - MySQL database connection 
      - https://netbeans.org/kb/docs/ide/mysql.html
 - JPA
      - https://www.youtube.com/watch?v=xgXypqbjaDs
 - JPQL 
      - https://en.wikipedia.org/wiki/Java_Persistence_Query_Language

## Testing

In order to test this web application you need to follow the various steps mentioned in the [wiki](https://github.com/Diogo525/MerchFinder/wiki) and add to the files:
- [TimerSessionBean.java](https://github.com/Diogo525/MerchFinder/tree/master/MerchFinder/src/main/java/com/ua/merchfinder/scheduler), line 85
- [GetEbaySearchResultsCtrl.java](https://github.com/Diogo525/MerchFinder/blob/master/MerchFinder/src/main/java/com/ua/merchfinder/controllers/GetEbaySearchResultsCtrl.java), line 117

&ndash; the Ebay Production or Sandbox Environment appID to the variable "appName".

## Creators

**Diogo Guedes**

- <https://github.com/Diogo525>

 
