# Spring Boot and Vue Kahoot-like app for Fullstack

## Overview


## Features

- User Authentication: Users can log in and register their accounts securely. This is solved through the use os cookies and refresh- and session tokens.
- Quiz Creation: Authenticated users have the possibility to create quizzes, adding and editing questions, setting quiz difficulty and add a quiz image representing the quiz. In addition, the user can use multiple types of questions (true or false and multiple choice).
- Tagging and Categories: When creating quiz, a user can add, or remove, categories for a specific quiz.
- Search and filtering: User can search and filter on quizzes, their categories and difficulties.
- Quiz templates: When loading a new quiz, a template quiz is presented such as the user can see how quizzes should be structured and presented.
- Collaborator: Author and other collaborators can add new collaborator to their quizzes. Collaborators and author have other possibilities than regular users. Collaborators can edit add and questions, and change quiz information. But only author can delete the quiz.
- Import and Export. Questions and entire quizzes can be exported and imported through the use of correctly structured csv files.
- Single player mode: Users have the possibility to find and play through a quiz of their choice. The user is updated on what answers are right and false, and their overall score. The user can then write a review and score the quiz which is then saved and stored in the database, but the public profile page was down prioritized and is therefore not implemented in the final code.
- Multiplayer mode: One user has the possibility to start a web-socket server that other players can connect to through a game-pin. The host sees the scoreboard and the questions while the players only see their own score and the questions. If a user doesn't have an account he can still join a game but must choose a username and avatar representation.
- Randomization: On single-player the question order is randomized, while on multiplayer both the question and the answers are randomized every game.
- Progress Tracking. The user can see his own score while playing a quiz. The final score is then displayed when the quiz is finished.


## Prerequisites

- Java
- Node.js
- Vue 3

## Getting Started

### Clone the repository

```
git clone -b development git@github.com:TrymNOHG/full-stack-2.git```
```
### Run the backend

1. Open Docker Hub

2. Navigate to the `backend` directory

```
cd backend
```
3. Install and run maven and data-base
```
 docker-compose up --build
```

4. Run the application

```
.
```

### Run the frontend

1. Navigate to the `frontend` directory

```
cd frontend
```

2. Install dependencies

```
npm install
```

3. Run the application

```
npm run dev
```

This will open a localhost with the port `/5173`

### Run tests

frontend test can be ran through these commands

```
cd frontend
npm install
npm run test:unit
npx cypress run --e2e
```

backend test can be ran through these commands

```
cd backend
mvn clean test
```

This will open a localhost with the port `/5173`

### Access the application

Open your browser and navigate to http://localhost:5173, or press `o` in the command window where
dev was accessed
