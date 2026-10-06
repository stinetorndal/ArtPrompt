# API Documentation - ArtPrompt

Base URL: `http://localhost:7070`

| Method   | URL                               | Request Body (JSON)        | Response (JSON)              | Error (e) |
|:---------|:----------------------------------|:---------------------------|:-----------------------------| :--- |
| **POST** | `/api/v1/auth/register`           | `UserDTO(email, password)` | `UserDTO(email, password)`   | `(e1)` 400 Bad Request*(f.eks. email findes)*
| **POST** | `/api/v1/auth/login`              | `UserDTO(email, password)` | `UserDTO(email, null)`       | `(e1)` 400 Bad Request*(f.eks. email findes)*
| **GET**  | `/api/v1/prompts/categories`      |                            | `List["MOOD", "COLOR", etc]` | `(e2)` 500 Internal Server error
| **GET**  | `/api/v1/prompts/random`          |                            | `PromptDTO(word 1, word 2)`  | `(e3)` 400 Bad Request / 404 Not Found
| **GET**  | `/api/v1/unsplash/color(=YELLOW)` |                            | `List`                       | `(e1/e2)` 400/500 
| **POST** | `/api/v1/images/save`             | `SavedImageDTO`            | `SavedImageDTO`              | `(e4/e1)` not found / bad request
| **GET**  | `/api/v1/images/user/{userid}`    | `{userid}`                 | `List<SavedImage>`           | `(e4)` not found
| **POST** | `/api/v1/notes/save`              | `NoteDTO`                  | `NoteDTO`                    | `(e4/e1)` not found / bad request
| **GET**  | `/api/v1/notes/user/{userid}`     | `{userid}`                 | `List<Note>`                 | `(e4)` not found