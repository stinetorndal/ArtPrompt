# API Documentation - ArtPrompt

Base URL: `http://localhost:7070`

| Method | URL                          | Request Body (JSON)        | Response (JSON) | Error (e) |
| :--- |:-----------------------------|:---------------------------| :--- | :--- |
| **POST** | `/api/v1/auth/register`      | `UserDTO(email, password)` | `UserDTO(email, password)`  | `(e1)` 400 Bad Request
| **POST** | `/api/v1/auth/login` | `UserDTO(email, password)` | `UserDTO(email, password)`  | `(e1)` 400 Bad Request