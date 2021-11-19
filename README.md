# FileTextReader
This file text reader is an application built by Jason Tse with Spring Boot.

It provides a REST API for uploading a file containing a section of text/story, and returns statistics around the length of words, average word length, and their frequencies.

## Installation
Simply clone the repository from Github.

To run the application in a terminal window
```
.\mvnw spring-boot:run
```

## Assumptions on the following must all be true for a word
- A word is a group of characters within a string that is separated by one or more spaces.
- If a word that contains alphanumeric characters and the ending characters are punctuation `.,;!?\"-`, then do not regard the punctuation as part of the word, e.g. "morning,", "following:"". This is because there are words that may end in a symbol that is part of the word, e.g. "days'", "1.2%". Also disregard a `"` that preceeds a word.
- For any words that does not contain alphanumeric characters, then treat them as a word, e.g. "******" and "&"

## Usage
Submit a GET request to the the /file endpoint on http://localhost:8080, with file as the form data.

We may use a REST API client, such as Postman, Insomnia, Paw, etc.

Alternatively with Curl,
```
curl --request GET http://localhost:8080/file --form file=@C:/Users/User/data.txt
```

The file must a file with content type 'text/plain'.

The current max file and request size is set to 10MB, this may be changed in /src/main/resources/application.properties

## Tests
Unit tests may be run with
```
.\mvnw clean test
```
