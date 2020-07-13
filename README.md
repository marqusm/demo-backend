# demo-backend-rest-java-spring-boot

This is a demo project implementing REST API of arbitrary project.

## Project requirements

### Overview
This project is a picture marketplace where users can upload, present and sell their own pictures.
The most liked pictures can be purchased by other users. Only the most liked pictures are available for purchase by the community.
When a user purchases their desired picture, they will receive a physical print  delivered to their home.

### Actors
System, Admin, Seller and Buyer

### Use cases
- Seller uploads a picture, a picture can have hashtags.
- Each picture has to be approved by Admin before it is available for displaying.
- When adding hashtags to a picture, the top hashtags will be suggested to the Seller, in an auto completion kind of way.
- Seller/Buyer likes a picture.
- Seller picture receives a minimum amount of likes to be available for purchase in the marketplace, Admin approves.
- Seller picture receives a minimum amount of likes to be available for purchase in the marketplace, Admin disapproves due to terms and conditions.
- Seller can edit/delete their picture from the marketplace.
- Buyer buys picture: When a Buyer buys picture he has to enter his basic information (address, full name etc).
- The Buyer payment executes via an external provider (e.g. PayPal).
- Our System stores the transaction ID of the external provider.
- The payment/transaction information has to be stored to generate reports.
- The System processes the purchase order of a picture to that it can be printed and sent to Buyer via mail.

### Tasks
- Design an SQL / NoSQL schema for the platform.
- Design the API endpoints for the platform.
- Implement an Authentication and Authorization system for the platform (User registration and login).
- Implement the Entities
- Implement the API endpoint to upload a picture.
- Implement the API endpoint and cache (hand-rolled cache) for storing the hash tags of the pictures.
- Implement a CRUD Generic library to access the database

## Running service
To run the service, please execute the following commands
```
./gradlew clean build
docker-compose up -d
java -jar build/libs/demo-backend-rest-java-springboot.jar
```

## Maintaining the project
To format code, run the following command:
```
./gradlew spotlessApply
```

To check for new dependencies versions, run the following:
```
./gradlew dependencyUpdates
```

## To consider in future
- Messages are not perfect. Message takes content of 'getMessage' exception method, which leads to framework exposing.
- More endpoints should be implemented to have complete API
- No cleaning data before/after running tests
- Passwords not hashed
- Database framework cannot run outside of web request
