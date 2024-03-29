Java Board Project
===
> Basic CURD example with spring boot
> > JPA, spring-security, spring-hateoas, spring-rest-docs
---

## Run test cases and extract test coverage

```gradle
./gradlew test
```

> Run junit Test cases with jacoco and test coverage result report by html

![image](https://user-images.githubusercontent.com/14158670/142552514-a6ff2ef5-a446-4a54-a20f-5ffa8697bbca.png)

- reports path : {your repository path}/build/reports/jacoco/test/html/index.html

---
## Generated API Docs with Spring REST Docs + OpenAPI3 + Redoc
refer URI : [github : restdocs-api-spec](https://github.com/ePages-de/restdocs-api-spec)

add redoc dependencies
```groovy
plugins {
    id 'org.asciidoctor.jvm.convert' version '3.3.2'
    id 'com.epages.restdocs-api-spec' version '0.11.5'
}

dependencies {
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    testImplementation 'com.epages:restdocs-api-spec-mockmvc:0.11.5'
    asciidoctorExtensions 'org.springframework.restdocs:spring-restdocs-asciidoctor'
}

openapi3 {
    servers = [ { url = "http://some.api-host" } ]
    title = 'My API title'
    version = '1.0.1'
    format = 'yaml'
    separatePublicApi = true
    outputFileNamePrefix = 'my-api-spec'
    oauth2SecuritySchemeDefinition = {
        flows = ['authorizationCode']
        tokenUrl = 'http://example.com/token'
        authorizationUrl = 'http://example.com/authorize'
        scopeDescriptionsPropertiesFile = "scopeDescriptions.yaml"
    }
}

```

change create document setting
```
AS-IS : 
- MockMvcRestDocumentation.document()
- MockMvcRequestBuilders.*
  - get(), post(), patch(), delete() ...

TO-BE : - MockMvcRestDocumentationWrapper.doc
- RestDocumentationRequestBuilders.*
  - get(), post(), patch(), delete() ...
```

```groovy
./gradlew clean build openapi3
```
> generate openapi3.yaml for OAS spec document

```groovy
npm install redoc-cli
```
> install with your run or ci environment

```groovy
cd {repository path}/build/api-spec
npx redoc-cli bundle openapi3.yaml
```
> export it to a static HTML file

What do you think useful as an API documentation tools
 - Spring REST Docs : http://localhost:8084/docs/rest-docs.html
   - ![img.png](img.png)
 - OAS3 with Redoc : http://localhost:8084/docs/redoc.html
   - ![img_1.png](img_1.png)
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

---
## Configuration
 - [Spring REST Docs snippet template](https://gist.github.com/choi-ys/8d7c76968fb6a0d272a084f8ff46daaf)
