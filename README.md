# GitHubApiClient_WebFlux_DEMO

## AUTHOR: JAKUB DUDEK

<br>


In case of any questions, I am available at the e-mail `jakub.dudek94@gmail.com` and no. phone `+48 783 963 469`.

<br>

## ABOUT:
This is a demo application that uses the WebClient (WebFlux approach) to connect to the GitHub Rest API and retrieve data on the user's repositories.
<br><br>
The application uses an asynchronous, non-blocking reactive approach (Project Reactor library), where requests are processed in a data stream based on the so-called Even Loop.
<br><br>
The main assumption of the project is to provide an API that, using GitHub Rest API v.3 as a backing-api, enables obtaining processed information on the user's repositories.
<br><br>

This is a main solution to the functional requirements listed below:

![image](https://github.com/DudekJakub/GitHubApiClient_Kohsuke_DEMO/assets/90628819/1e64b04e-ec24-4649-8ffa-a6f525f39b0b)

<br>

Side solution is available here: https://github.com/DudekJakub/GitHubApiClient_Kohsuke_DEMO

<br>

## USED DEPENDENCIES:
||
| --------------- |
| WebFlux-starter |
| Validation-starter |
| Aop-starter |
| lombok |

<br>

## PROJECT STRUCTURE/DESCRIPTION:

The project was written in version 17 of the JAVA language.
<br><br>
Used build-toop: `gradle`
<br><br>
The application is created in the style of `Restful API`.
<br><br>
In order to optimize the work on the project, the `Spring Boot` framework (Spring Framework subproject) was used. The main reasons for using this framework in the context of the presented application are: relieving the user from manual management of the object life cycle (the entire Invertion of Control mechanism (container), Depenency Inversion and Dependency Injection tool), built-in default support for the Netty server (in the case of synchronous applications it is Tomcat servlet server), simplified dependency management (so-called starter packs), automatic DispatcherServlet configuration, etc.
<br><br>
The project has been divided into several packages. The main package containing the most important business logic is the `service` package.
<br><br>
Responses from the github API are transformed into model objects, which at the final stage are mapped to DTO objects containing only the desired information.
<br><br>
In order to isolate dependencies, both service classes and mappers implement corresponding interfaces. Thanks to this approach, the project follows the basic principles of working with an object-oriented language (mainly SOLID principles). 
<br><br>
Logging for application debugging takes place in a specially prepared class based on the aspect approach (`LoggingAspect.class`). This approach allows you to follow the so-called `Separation of Conservation` (SoC). Logger @Slf4j from the lombok library was used as a logging mechanism. In order to disable/enable logging in the LoggingAspect class, the flag in the application.properties file must be set accordingly.
<br><br>
Dependencies (beans) are injected everywhere through the constructor, which is marked with the @Autowired annotation (this annotation is not currently required, but due to the personal preferences of the project author, it is included in the code).
<br><br>
Exceptions arising when a request/code error occurs when using API endpoints are handled in the `GlobalExceptionHandle`r class, which is focused on returning properly formulated information to the client.
<br><br>
Due to the `Restfull API` approach, the current controller has been marked with @RestController annotation. Endpoints return JSON representations of resources by default (XML format NOT CURRENTLY SUPPORTED).
<br>
In addition, the controller was written in accordance with the so-called Richardson maturity (currently level 2), with the ability (for the future) to flexibly create endpoint paths, etc.
<br><br>
Beans are created with the so-called "programmatically" approach. The application therefore does not have any XML-based configuration.
<br><br>
In order to protect sensitive information such as GITHUB_TOKEN, information about the token in the `application.properties` file is hidden behind the so-called. environment variable.

## TEST ENVIRONMENT:
Tests cover both unit and intrusive cases.
To properly use integration tests based on `WebTestClient`, a token (JWT) is required to authenticate the user.
Instructions for obtaining and entering the token into the application are presented in the `HOW TO USE` section.

<br>

# HOW TO USE:

In order to run the application, Java version 17 and build-tool gradle are required.
<br><br>
In addition, to authenticate an application using GitHub Rest API v.3, it is required to generate the so-called. JsonWebToken. Instruction below:

`STEP 1`
<br>
Enter your GitHub account and click on your avatar picture:
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/89b03ce7-bde1-4dfb-9862-dd979fdebba0)

<br>

`STEP 2`
<br>
Pick `settings` option:
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/b9ada165-8ea3-4948-86be-9d4d79e6c551)

<br>

`STEP 3`
<br>
On the left vertical list of options go to the very last one called `Developer Settings`:
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/09a5b98f-fc3c-447a-9404-2e419946a20e)

<br><br>

`STEP 4`
<br>
Now click `Personal access tokens` option:
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/d58eb11d-e20e-4673-a100-152e64f0481c)

<br>

`STEP 5`
<br>
Pick `Fine-grained tokens` option:
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/eb7c2cd4-789e-4fdc-9b1e-7a2632754444)

<br>

`STEP 6`
<br>
Click `Generate new token` button:
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/f7a38f21-6080-45d3-8273-aa2b9e5578bc)

<br>

`STEP 7`
<br>
Confirm your credentials.

<br>

`STEP 8`
<br>
Provide token name and expiration time then pick:
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/19c92556-3272-498b-9e2e-98be9c13b583)
<br>
...from `Permissions` list pick `Repository permissions` and grant access to following options: `actions`,`code`,`metadata`.

<br>

`STEP 9`
<br>
Now when your token is generated, copy it and paste it into application's environmental variables (with the name of variable `GITHUB_JWT=`):
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/58fdc23f-83a8-4bc2-abf3-1cd7127a3d13)
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/e65ec081-47ba-4a84-a1d3-27157c05e445)
<br>
![image](https://github.com/DudekJakub/GitHubApiClient_WebFlux_DEMO/assets/90628819/efeb35b5-a018-48ec-950e-74089e877397)

<br><br>

To create Windows system env. variable please following this link: 
<br>
https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html

<br><br>

### IMPORTANT!
In case of integration tests JWT token need to be provided to TestClass run options (as env. variable) or for particular test if the test is about run by itself.
