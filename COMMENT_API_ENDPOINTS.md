# Comment API Endpoints Summary

## Base URL
All comment endpoints are prefixed with: `/api/comment`

## Authentication
- All endpoints require valid JWT authentication
- User information is extracted from the JWT token's `preferred_username` claim

## Endpoints

### 1. Create Comment
- **Method**: `POST`
- **URL**: `/api/comment`
- **Authentication**: Required
- **Request Body**: 
  ```json
  {
    "text": "string",
    "pageId": "number"
  }
  ```
- **Response**: 
  - **Status**: `201 CREATED`
  - **Body**: 
    ```json
    {
      "id": "number",
      "text": "string",
      "createdAt": "LocalDateTime",
      "utilisateurLogin": "string (extracted from JWT)",
      "pageId": "number"
    }
    ```
- **Description**: Creates a new comment associated with a page. The user login is automatically extracted from the JWT token.

### 2. Get All Comments (Paginated)
- **Method**: `GET`
- **URL**: `/api/comment`
- **Authentication**: Required
- **Query Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)
  - `sort`: Sort criteria (e.g., `createdAt,desc`)
- **Response**: 
  - **Status**: `200 OK`
  - **Body**: 
    ```json
    {
      "content": [
        {
          "id": "number",
          "text": "string",
          "createdAt": "LocalDateTime",
          "utilisateurLogin": "string",
          "pageId": "number"
        }
      ],
      "pageable": {...},
      "totalElements": "number",
      "totalPages": "number",
      "last": "boolean",
      "first": "boolean"
    }
    ```
- **Description**: Retrieves all comments with pagination support.

### 3. Search Comments
- **Method**: `GET`
- **URL**: `/api/comment/search`
- **Authentication**: Required
- **Query Parameters**:
  - `q`: Search query (required) - searches in comment text
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)
  - `sort`: Sort criteria
- **Response**: 
  - **Status**: `200 OK`
  - **Body**: Same paginated structure as Get All Comments
- **Description**: Searches comments by text content (case-insensitive).

### 4. Get Comment by ID
- **Method**: `GET`
- **URL**: `/api/comment/{id}`
- **Authentication**: Required
- **Path Parameters**:
  - `id`: Comment ID (Long)
- **Response**: 
  - **Status**: `200 OK`
  - **Body**: 
    ```json
    {
      "id": "number",
      "text": "string",
      "createdAt": "LocalDateTime",
      "utilisateurLogin": "string",
      "pageId": "number"
    }
    ```
- **Description**: Retrieves a specific comment by its ID.

### 5. Update Comment
- **Method**: `PUT`
- **URL**: `/api/comment/{id}`
- **Authentication**: Required
- **Path Parameters**:
  - `id`: Comment ID (Long)
- **Request Body**: 
  ```json
  {
    "text": "string",
    "pageId": "number (optional)"
  }
  ```
- **Response**: 
  - **Status**: `200 OK`
  - **Body**: Updated CommentDTO
- **Description**: Updates an existing comment. Can update both text and associated page.

### 6. Delete Comment
- **Method**: `DELETE`
- **URL**: `/api/comment/{id}`
- **Authentication**: Required
- **Path Parameters**:
  - `id`: Comment ID (Long)
- **Response**: 
  - **Status**: `204 NO CONTENT`
  - **Body**: Empty
- **Description**: Deletes a comment by its ID.

## Comment Data Structure

### CommentDTO Fields:
- `id`: Unique identifier (Long)
- `text`: Comment content (String)
- `createdAt`: Creation timestamp (LocalDateTime)
- `utilisateurLogin`: Username of the comment author (String)
- `pageId`: ID of the associated page (Long)

## Error Handling
- **404 Not Found**: When comment or page is not found
- **401 Unauthorized**: When JWT token is invalid or missing
- **400 Bad Request**: When request data is invalid

## Relationships
- **Many-to-One**: Comment belongs to a Page entity via `pageId`
- **User Association**: Comments are linked to users via `utilisateurLogin` extracted from JWT

## Notes
- All timestamps are in LocalDateTime format
- Search functionality is case-insensitive and searches within comment text
- Pagination follows Spring Data conventions
- User identification is automatic through JWT token authentication
