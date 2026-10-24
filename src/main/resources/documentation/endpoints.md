# API Documentation - ArtPrompt

Base URL: `http://localhost:7070`

| Method   | URL                                              | Request Body (JSON)        | Response (JSON)              | Error (e)                                                      |
|:---------|:-------------------------------------------------|:---------------------------|:-----------------------------|:---------------------------------------------------------------|
| **POST** | `/api/v1/auth/register`                          | `UserDTO(email, password)` | `UserDTO(email, password)`   | `(e1)` 400 Bad Request *(e.g. email already exists)*           |
| **POST** | `/api/v1/auth/login`                             | `UserDTO(email, password)` | `UserDTO(email, null)`       | `(e1)` 400 Bad Request *(e.g. wrong email or password)*        |
| **GET**  | `/api/v1/prompts/categories`                     |                            | `List["MOOD", "COLOR", etc]` | `(e2)` 500 Internal Server Error                               |
| **GET**  | `/api/v1/prompts/random?cat1=STYLE&cat2=SUBJECT` |                            | `PromptDTO(word 1, word 2)`  | `(e3)` 400 Bad Request / 404 Not Found                         |
| **GET**  | `/api/v1/unsplash/color?color=RED`               |                            | `List`                       | `(e1/e2)` 400 Bad Request / 500 Internal Server Error          |
| **POST** | `/api/v1/images/save`                            | `SavedImageDTO`            | `SavedImageDTO`              | `(e4/e1)` 404 Not Found / 400 Bad Request                      |
| **GET**  | `/api/v1/images/user/{userid}`                   |                            | `List<SavedImage>`           | `(e4)` 404 Not Found                                           |
| **POST** | `/api/v1/notes/save`                             | `NoteDTO`                  | `NoteDTO`                    | `(e4/e1)` 404 Not Found / 400 Bad Request                      |
| **GET**  | `/api/v1/notes/user/{userid}`                    |                            | `List<Note>`                 | `(e4)` 404 Not Found                                           |
| **GET**  | `/api/v1/rijksmuseum/artist?artist=REMBRANDT`    |                            | `List`                       | `(e1/e2)` 400 Bad Request / 500 Internal Server Error          |
| **GET**  | `/api/v1/rijksmuseum/fetch-all`                  |                            | `FetchResultDTO`             | 401 Unauthorized / 403 Forbidden / 409 Conflict / 503 Service Unavailable |
| **POST** | `/api/v1/paintings/save`                         | `SavedPaintingDTO`         | `SavedPaintingDTO`           | `(e4/e1)` 404 Not Found / 400 Bad Request                      |
| **GET**  | `/api/v1/paintings/user/{userid}`                |                            | `List<SavedPainting>`        | `(e4)` 404 Not Found                                           |
| **GET**  | `/api/v1/paintings/random`                       |                            | `List<SavedPaintingDTO>`     | `(e4/e1)` 404 Not Found / 400 Bad Request                      |
| **GET**  | `/api/v1/routes`                                 |                            | HTML page with all routes    |                                                                |

All `/api/*` endpoints are rate limited to 20 requests per minute per IP (429 Too Many Requests).