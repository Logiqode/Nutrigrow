# NutriGrow API Documentation

## Base URL
`https://your-domain.com/api`

## Authentication
Most endpoints require authentication. Use the `/user/login` endpoint to obtain credentials.

## Error Responses
- `400 Bad Request`: Invalid request parameters
- `401 Unauthorized`: Invalid or missing authentication
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

---

## User Management

### Register a new user
**Endpoint**: `POST /user/register`

**Request Body**:
```json
{
  "email": "string",
  "password": "string",
  "username": "string",
  "name": "string",
  "address": "string",
  "phone": "string",
  "gender": "char (M/F)"
}
```

**Response**:
- Success: `200 OK` with user details
- Error: `400 Bad Request` for invalid data

### Login
**Endpoint**: `POST /user/login`

**Request Body**:
```json
{
  "username": "string",
  "password": "string"
}
```

**Response**:
- Success: `200 OK` with user credentials
- Error: `401 Unauthorized` for invalid credentials

### Get all users
**Endpoint**: `GET /user`

**Response**: List of all users

### Get user by ID
**Endpoint**: `GET /user/{id}`

**Response**: User details

### Update user
**Endpoint**: `PUT /user/{id}`

**Request Body**: User details

### Delete user
**Endpoint**: `DELETE /user/{id}`

---

## Food Ingredients (Bahan Makanan)

### Get all ingredients
**Endpoint**: `GET /bahan-makanan`

**Response**: List of all food ingredients

### Get ingredient by ID
**Endpoint**: `GET /bahan-makanan/{id}`

### Create new ingredient
**Endpoint**: `POST /bahan-makanan`

**Request Body**: Ingredient details

### Update ingredient
**Endpoint**: `PUT /bahan-makanan/{id}`

### Delete ingredient
**Endpoint**: `DELETE /bahan-makanan/{id}`

---

## Food Items (Makanan)

### Get all food items
**Endpoint**: `GET /makanan`

**Response**: List of all food items

### Get food item by ID
**Endpoint**: `GET /makanan/{id}`

### Create new food item
**Endpoint**: `POST /makanan`

**Request Body**: Food item details

### Update food item
**Endpoint**: `PUT /makanan/{id}`

### Delete food item
**Endpoint**: `DELETE /makanan/{id}`

### Manage ingredients for food item
- Get ingredients: `GET /makanan/{id}/bahan`
- Add ingredient: `POST /makanan/{id}/bahan`
- Remove ingredient: `DELETE /makanan/{id}/bahan`

---

## News (Berita)

### Get all news
**Endpoint**: `GET /berita`

**Query Parameters**:
- `categoryId`: Optional filter by category

### Get news by ID
**Endpoint**: `GET /berita/{id}`

### Create news
**Endpoint**: `POST /berita`

**Request Body**: News details

### Update news
**Endpoint**: `PUT /berita/{id}`

### Delete news
**Endpoint**: `DELETE /berita/{id}`

---

## Stunting Data

### Get all stunting records
**Endpoint**: `GET /stunting`

**Query Parameters**:
- `userId`: Optional filter by user ID

### Get stunting record by ID
**Endpoint**: `GET /stunting/{id}`

### Create stunting record
**Endpoint**: `POST /stunting`

**Request Body**: Stunting record details

### Update stunting record
**Endpoint**: `PUT /stunting/{id}`

### Delete stunting record
**Endpoint**: `DELETE /stunting/{id}`

### Predict stunting risk
**Endpoint**: `POST /stunting/predict`

**Request Parameters**:
- `jenisKelamin`: 0 (male) or 1 (female)
- `height`: Height in cm
- `ageInMonths`: Age in months

**Response**: Prediction result