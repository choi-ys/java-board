Java Board Project
===
> Basic CURD example with spring boot
> > JPA, spring-security, spring-hateoas, spring-rest-docs
---

## Run test cases
```gradle
./gradlew test
```

![image](https://user-images.githubusercontent.com/14158670/138407815-7aa44828-84fa-4d4c-a527-b2a4c7b5f146.png)
- reports path : {your repository path}/build/reports/tests/test/index.html
---

## Run build
```gradle
build : ./gradlew clean build
run : java -jar {your repository path}/build/libs/java-board-0.0.1-SNAPSHOT.jar
```
---

## Package structure
```
└─src
    ├─docs
    │  └─asciidoc : base api docs format
    ├─main
    │  ├─java
    │  │  └─io
    │  │      └─example
    │  │          └─board
    │  │              ├─advice
    │  │              │  └─exception : Global exception handler
    │  │              ├─config
    │  │              │  ├─application : Beans configurations required to run the spring application 
    │  │              │  ├─jpa : Data-jpa AuditorAware configurations
    │  │              │  ├─p6spy : JPA execute query print configurations
    │  │              │  ├─redis : Embedded redis configurations in local, test profiles
    │  │              │  └─security : Spring-security, java-jwt configurations
    │  │              │      ├─endpoint : Http Request security configurations
    │  │              │      └─jwt : jwt security configurations
    │  │              ├─controller : Request router
    │  │              ├─domain : Service domain model
    │  │              │  ├─dto : Data Transfer Object
    │  │              │  ├─rdb : JPA entity
    │  │              │  ├─redis : Redis entity
    │  │              │  └─vo : Value object
    │  │              ├─repository
    │  │              │  ├─rdb : Rdb connect interface
    │  │              │  └─redis : Redis connect interface
    │  │              ├─service : Business transaction 
    │  │              └─utils
    │  └─resources
    │      └─static
    │          └─docs : Generated api docs by spring-rest-docs
    └─test : application test cases
```