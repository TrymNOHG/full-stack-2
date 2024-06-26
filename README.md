# Quibble
## Table of contents
- [Introduction](#introduction)
- [Pre-requisites](#pre-requisites)
- [Start application](#start-application)
    - [Database](#database)
    - [Backend](#backend)
    - [Frontend](#frontend)
- [Tests](#tests)
    - [Frontend Tests](#frontend-tests)
    - [Backend Tests](#backend-tests)

## Introduction
Quibble is a quiz application that allows users to create and share their quizzes with the world!
Quizzes can ble played in single player or multiplayer. Multiplayer quizzes are played in real-time with other users
in the same room.

## Pre-requisites
These are the tools you need to have installed in your machine to run the application:
- Docker
- Docker-compose
- npm
- Node.js
- maven
- java 17


# Start application
If you would like to develop this project further, this is how you can start the application.
First clone the repository.
```shell
git clone git@github.com:TrymNOHG/full-stack-2.git;
cd full-stack-2
```
### Database
The database is run in docker with the following command:
```shell
docker-compose up db -d
```
### Backend
The backend can be run with the following commands:

```shell
cd backend;
mvn spring-boot:run
```

### Frontend
In another terminal, go to the project and navigate to the frontend folder: 
```shell
cd frontend;
npm install;
npm run dev
```
Open your browser and navigate to http://localhost:5173, or press `o` in the command window where
dev was accessed

# Tests

## Frontend Tests
frontend test can be run through these commands:

```
cd frontend
npm install
npm run test:unit
npx cypress run --e2e
```

## Backend Tests
backend test can be run through these commands;

```
cd backend
mvn clean test
```

