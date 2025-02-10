## Database: 

You must have either a running postgresql instance in your local machine or create one using the provided docker compose file

## API configuration:

you must declare these variables to connect the api to a running database. 
/!\ update values to fit with your running instance

DB_PASSWORD=root;
DB_URL=jdbc:postgresql://localhost:5434/kata;
DB_USERNAME=root

## Scripts to be executed:

`CREATE TYPE transaction_type AS ENUM ('DEPOSIT', 'WITHDRAW');`

## Build the API:

`./gradlew build` 

## Run the API :

`./gradlew bootRun --args='--DB_PASSWORD=root --DB_URL=jdbc:postgresql://localhost:5434/kata --DB_USERNAME=root'`

## Run the app and inject some test data at startup time using runtime spring profile **_auto_data_injection_**:

`./gradlew bootRun --args='--spring.profiles.active=auto_data_injection --DB_PASSWORD=root --DB_URL=jdbc:postgresql://localhost:5434/kata --DB_USERNAME=root'`

We are aware of the following issues:

issue 1: the api is using a custom postgresql object type called **transaction_type**,
it's working correctly, however hibernate creates also another time called **transactiontype**.
we should find a way to disable automatic creation of enum type.

issue 2: The API is still missing unit tests



