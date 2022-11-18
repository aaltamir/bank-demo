# bank-demo
Just a simple bank demo to show some basic functionalities of Spring Boot

### Building and testing

The application uses Maven and the maven wrapper configuration is included. In order to compile and run unit/integration
tests, the following command should be executed in the root of the project after its checkout:

> ./mvnw clean test

### Running the application

> ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8080

Change the port to a different number if 8080 is busy in your computer.

### Use of the aplication

The best way to use the APIs provided by the application is running the Swagger UI, that should be available in the following
URL:

> http://localhost:8080/swagger-ui/index.html

Here the following endpoints are available:

To create one account for a customer:

> POST /api/customer/{customerId}/account <br>
> {<br>
> "initialCredit": 0<br>
> }<br>

To get the summary for a customer:
> GET /api/customer/{customerId}/summary

To more details about the API use the swagger UI specified previously.

Customers are created automatically when the application starts. Any customerId between 1 and 100 can be used.

### Some relevant design decisions

* The application is a demo that uses database IN MEMORY. This means that all data will be lost if the application is restarted.
* Several accounts can be created for the same customer.
* Negative credits are not supported
* Logs are displayed in the console of the application.


