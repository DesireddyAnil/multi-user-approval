# Multi-User Approval System

A Spring Boot application that implements a multi-user approval workflow system.

## Features

- User authentication with JWT
- Task creation with multiple approver assignment
- Approval workflow with notifications
- Comment system for discussions

## Technologies Used

- Java 11/17
- Spring Boot
- Spring Security with JWT
- Spring Data JPA
- H2 Database
- Maven

## Setup and Running

### Prerequisites
- Java 11 or higher
- Maven

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run`
4. The application will be available at `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Tasks
- `POST /api/tasks` - Create a new task
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get a specific task
- `GET /api/tasks/my-tasks` - Get tasks created by current user
- `GET /api/tasks/pending-approval` - Get tasks pending current user's approval

### Approvals
- `POST /api/tasks/{id}/approve` - Approve a task
- `POST /api/tasks/{id}/reject` - Reject a task

### Comments
- `POST /api/tasks/{id}/comments` - Add a comment to a task
- `GET /api/tasks/{id}/comments` - Get comments for a task
