# Quibble
## Table of contents
- [Introduction](#introduction)
- [Features](#features)
- [Pre-requisites](#pre-requisites)
- [Start application](#start-application)
- [Getting started developing](#getting-started-developing)
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
## Features

- User Authentication: Users can log in and register their accounts securely. This is solved through the use of refresh- and session tokens. The refresh tokens are stored as secure http cookies.
- Quiz Creation: Authenticated users have the possibility to create quizzes, adding and editing questions, setting quiz difficulty and add a quiz image representing the quiz. In addition, the user can use multiple types of questions (true or false and multiple choice).
- Tagging and Categories: When creating quiz, a user can add, or remove, categories for a specific quiz.
- Search and filtering: User can search and filter on quizzes, their categories and difficulties.
- Quiz templates: When loading a new quiz, a template quiz is presented such as the user can see how quizzes should be structured and presented.
- Collaborator: Author and other collaborators can add new collaborator to their quizzes. Collaborators and author have other possibilities than regular users. Collaborators can edit add and questions, and change quiz information. But only author can delete the quiz.
- Import and Export. Questions and entire quizzes can be exported and imported through the use of correctly structured csv files.
- Single player mode: Users have the possibility to find and play through a quiz of their choice. The user is updated on what answers are right and false, and their overall score. The user can then write a review and score the quiz which is then saved and stored in the database, but the public profile page was down prioritized and is therefore not implemented in the final code.
- Multiplayer mode: One user has the possibility to start a web-socket server that other players can connect to through a game-pin. The host sees the scoreboard and the questions while the players only see their own score and the questions. If a user doesn't have an account he can still join a game but must choose a username and avatar representation.
- Randomization: On single-player the question order is randomized, while on multiplayer both the question and the answer orders are randomized every game.
- Progress Tracking. The user can see his own score while playing a quiz. The final score is then displayed when the quiz is finished.

## Pre-requisites
These are the tools you need to have installed in your machine to run the application:
- Docker
- Docker-compose
- npm
- Node.js
- maven
- java 17


# Start application
The easiest way to run the application is with docker. Simply run the install-script:
```shell
sh run.sh
```
Then go to http://localhost:5173 to try the application.

# Getting started developing
If you would like to develop this project further, this is how you can start the application i development mode.
First clone the repository.
```shell
git clone git@github.com:TrymNOHG/full-stack-2.git
```
### Database
The database is run in docker with the following command:
```shell
docker-compose up db
```
### Backend
The backend can be run with the following commands:

```shell
cd backend;
mvn run spring:boot
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

