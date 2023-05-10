Launch Voting Management Application
====================================

**The application is a result of graduation assignment performing for Java Spring Developer Course (TopJava). The project uses some cutting edge technologies, like Spring Boot 2.x, Lombok, JPA(Hibernate), H2, Jackson, JUnit. The assignment was given:**

Technical requirement:
----------------------
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
  * If it is before 11:00 we assume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it (better - link to Swagger).
___

How to use the application:
----------------------

To install use the commands:
 ```
 git clone https://github.com/smb13/voteforlunch.git
 ```
To run the application:
 ```
 mvn spring-boot:run
 ```

The Swagger page with REST API documentation will be available by link [REST API documentation](http://localhost:8080/swagger-ui/index.html)