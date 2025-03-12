
### 1. **Docker Container for Postgres**

```shell
docker login
docker pull postgres
docker run --name postgresSpringDB -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=students -d postgres
docker start postgresSpringDB
```

**Database Info (for DBeaver or SQL Developer)**
- **Host:** `localhost`
- **SID (Database):** `students`
- **Port:** `5432`
- **Username:** `admin`
- **Password:** `admin`

---

**Add JVM Arguments to Allow Access**  
If you must use a newer Java version, you can override the restrictions by adding the following JVM options:

For IntelliJ IDEA:  
`Run -> Edit Configurations -> Modify Options -> Add VM Options`

```shell
--add-opens=java.base/java.io=ALL-UNNAMED
```

---

### 2. **Example of Correct Postman Setup**

#### 2.1. **For Studenti Table**

**GET Request:**
- **URL:** `http://localhost:8080/api/studenti`

**POST Request:**
- **URL:** `http://localhost:8080/api/studenti`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "nume": "John Doe",
  "varsta": 20,
  "email": "john.doe@example.com",
  "specialitate": "Informatica",
  "anStudiu": 2,
  "medie": 8.5,
  "bursier": true,
  "dataInscrierii": "2025-03-01",
  "cursuri": []
}
```

**PUT Request:**
- **URL:** `http://localhost:8080/api/studenti/1`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "nume": "John Doe Updated",
  "varsta": 21,
  "email": "john.doe.updated@example.com",
  "specialitate": "Informatica",
  "anStudiu": 3,
  "medie": 8.8,
  "bursier": true,
  "dataInscrierii": "2025-03-01",
  "cursuri": [
    {
      "id": 1
    }
  ]
}
```

**PATCH Request:**
- **URL:** `http://localhost:8080/api/studenti/1`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "medie": 1.0
}
```

**DELETE Request:**
- **URL:** `http://localhost:8080/api/studenti/1`

---

#### 2.2. **For Profesori Table**

**GET Request:**
- **URL:** `http://localhost:8080/api/profesori`

**POST Request:**
- **URL:** `http://localhost:8080/api/profesori`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "nume": "John Doe",
  "materie": "Matematica",
  "experientaAni": 10,
  "cursuri": []
}
```

**PUT Request:**
- **URL:** `http://localhost:8080/api/profesori/1`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "nume": "John Doe Updated",
  "materie": "Matematica Avansata",
  "experientaAni": 12,
  "cursuri": []
}
```

**PATCH Request:**
- **URL:** `http://localhost:8080/api/profesori/1`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "experientaAni": 2
}
```

**DELETE Request:**
- **URL:** `http://localhost:8080/api/profesori/1`

---

#### 2.3. **For Cursuri Table**

**GET Request:**
- **URL:** `http://localhost:8080/api/cursuri`

**POST Request:**
- **URL:** `http://localhost:8080/api/cursuri`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "denumire": "Fizica Avansata",
  "credite": 6,
  "profesor": {
    "id": 1
  },
  "studenti": [
    {
      "studentId": 1
    }
  ]
}
```

**PUT Request:**
- **URL:** `http://localhost:8080/api/cursuri/1`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "denumire": "Matematica Avansata",
  "credite": 9,
  "profesor": {
    "id": 1
  },
  "studenti": [
    {
      "studentId": 1
    }
  ]
}
```

**PATCH Request:**
- **URL:** `http://localhost:8080/api/cursuri/1`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**

```json
{
  "credite": 3
}
```

**DELETE Request:**
- **URL:** `http://localhost:8080/api/cursuri/1`

---
