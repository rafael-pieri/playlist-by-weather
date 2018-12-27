# Overview

It is a micro-service able to accept RESTful requests receiving as parameter either city name or lat long coordinates and returns a playlist suggestion according to the current temperature.

## Business rules

* If temperature (celcius) is above 30 degrees, suggest tracks for party
* In case temperature is between 15 and 30 degrees, suggest pop music tracks
* If it's a bit chilly (between 10 and 14 degrees), suggest rock music tracks
* Otherwise, if it's freezing outside, suggests classical music tracks

### How to run the application

Execute the following command to deploy the application:

`sh initialize.sh`

It will available at: `http://localhost:8091`

### Swagger UI
```http://localhost:8091/swagger-ui.html```

### Postman Collection
Import the file ```/postman/playlist-by-weather.postman_collection.json```
