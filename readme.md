## The functional need of the Bank account kata:

Think of your personal bank account experience
When in doubt, go for the simplest solution

Requirements:

- Deposit and Withdrawal
- Account statement (date, amount, balance)
- Statement printing

User Stories

**US 1:**
In order to save money
As a bank client
I want to make a deposit in my account

**US 2:**
In order to retrieve some or all of my savings
As a bank client
I want to make a withdrawal from my account

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

## Run integration tests
Since we're using testContainers to simulate a real PostgreSQL instance,
Docker must be installed on the machine to run the integration tests correctly 

## Build the API:

`./gradlew build` 

## Run the API :

`./gradlew bootRun --args='--DB_PASSWORD=root --DB_URL=jdbc:postgresql://localhost:5434/kata --DB_USERNAME=root'`

## Run the app and inject some test data at startup time using runtime spring profile **_auto_data_injection_**:

`./gradlew bootRun --args='--spring.profiles.active=auto_data_injection --DB_PASSWORD=root --DB_URL=jdbc:postgresql://localhost:5434/kata --DB_USERNAME=root'`



