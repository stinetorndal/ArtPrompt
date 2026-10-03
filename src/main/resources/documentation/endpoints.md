# API Documentation - ArtPrompt

Base URL: `http://localhost:7070`

| Method      | URL                          | Request Body (JSON)        | Response (JSON)                                                       | Error (e) |
|:------------|:-----------------------------|:---------------------------|:----------------------------------------------------------------------| :--- |
| **POST**    | `/api/v1/auth/register`      | `UserDTO(email, password)` | `UserDTO(email, password)`                                            | `(e1)` 400 Bad Request*(f.eks. email findes)*
| **POST**    | `/api/v1/auth/login`         | `UserDTO(email, password)` | `UserDTO(email, null)`                                                | `(e1)` 400 Bad Request*(f.eks. email findes)*
| **GET**     | `/api/v1/prompts/categories` |                            | `List["MOOD", "COLOR", etc]`                                          | `(e2)` 500 Internal Server error
| **GET** | `/api/v1/prompts/random`     |                            | `PromptDTO(word 1, word 2)` | `(e3)` 400 Bad Request / 404 Not Found