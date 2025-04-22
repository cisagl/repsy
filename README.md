

Repsy Package Repository

A Java Spring Boot application that allows uploading and downloading `.rep` packages along with their metadata.

Requirements

- Java 17
- Maven
- PostgreSQL
- MinIO

Configuration

Add the following to `application.properties`:

```markdown
spring.datasource.url=jdbc:postgresql://localhost:XXX/repsy
spring.datasource.username=XXX
spring.datasource.password=XXX
spring.jpa.hibernate.ddl-auto=update

storage.strategy=object-storage

minio.url=http://localhost:XXX
minio.accessKey=XXX
minio.secretKey=XXX
minio.bucket=repsy
```


API Endpoints

POST /{name}/{version}

Uploads a `.rep` package and its corresponding `meta.json` file.

- Request type: multipart/form-data
- Required fields:
  - package.rep (binary file)
  - meta.json (metadata in JSON format)

GET /{name}/{version}/{filename}

Downloads a previously uploaded file. The filename can be `package.rep` or `meta.json`.

MinIO Setup with Docker

To run MinIO locally:


```
docker run -p 9000:9000 -p 9001:9001 --name minio ^
-e "MINIO_ROOT_USER=minioadmin" ^
-e "MINIO_ROOT_PASSWORD=minioadmin" ^
-v minio_data:/data ^
quay.io/minio
```
```
MinIO console: http://localhost:9001  
Username: minioadmin  
Password: minioadmin
```
