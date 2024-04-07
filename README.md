# Spring Boot and Vue Kahootlike app for Fullstack

## Overview


## Features

- Add, edit and remove items from the virtual fridge inventory
- Plan meals by ingredients from the virtual fridge inventory
- Generate a shopping list based on the planned meals and the current virtual fridge inventory
- Get dinner suggestions based on the available ingredients in the virtual fridge inventory
- User authentication and authorization
- Responsive UI design
- Integration with grocery stores APIs to retrieve real-time prices and availability
- Implementation of a notification system to remind users of expiring items


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
