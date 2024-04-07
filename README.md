# Quibble
## Table of contents
- [Introduction](#introduction)
- [Run application](#run-application)
  - [Pre-requisites](#pre-requisites)
  - [Start application](#start-application)
- [Features](#features)
- [Technical feats](#technical-feats)

## Introduction
Quibble is a quiz application that allows users to create and share their quizzes with the world! 
Quizzes can ble played in single player or multiplayer. Multiplayer quizzes are played in real-time with other users
in the same room.

## Run application

### Pre-requisites
These are the tools you need to have installed in your machine to run the application:
- Docker
- Docker-compose
- npm
- maven
- java 17

### Start application
The easiest way to run the application is with docker. Simply run the install-script:
```shell
sh run.sh
```
Then go to http://localhost:5173 to try the application.

## Features

- Quiz management and creation
- User management
- Multiplayer and single player quizzes

## Technical feats
- Refresh tokens and jwt tokens for authentication
- Websockets for multiplayer quizzes
- Quiz collaboration with other users