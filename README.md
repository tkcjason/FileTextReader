# FileTextReader
This file text reader is an application built by Jason Tse with Spring Boot for SYNALOGiK.

## Installation
Simply clone the repository from Github.

To run the application in a terminal window
```
.\mvnw spring-boot:run
```

## Usage
Submit a POST request to the the /file endpoint on http://localhost:8080, with file as the form data.

We may use a REST API client, such as Postman, Insomnia, Paw, etc.

Alternatively with Curl,
```
curl -v -F file=@C:/Users/User/data.txt http://localhost:8080/file
```

The file must a file with content type 'text/plain'.

The current max file and request size is set to 1MB, this may be changed in /src/main/resources/application.properties
