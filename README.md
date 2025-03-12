### 1. Docker Container for Postgres

```shell
docker login

docker pull postgres
docker run --name postgresSpringDB -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=students -d postgres

docker start postgresSpringDB
```

**Database Info (for DBeaver or SQL Developer)**

*Host: `localhost`*

*SID (Database): `students`*

*Port: `5432`*

*Username: `admin`*

*Password: `admin`*