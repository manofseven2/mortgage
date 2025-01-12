# Mortgage check application 

## System Design Diagram

![System design diagram](src/main/docs/mortgage.png)

## Build

```shell
./mvnw clean package
```

with ignoring tests

```shell
mvn clean install -DskipTests
```

Running tests

```shell
./mvnw veryfy
```

## Docker

### Building docker image:

```shell
./mvnw docker:build
```

### running docker container:

```shell
docker-compose up -d
```

### Stopping the container

```shell
docker-compose down
```

### API documentation

http://localhost:8080/swagger-ui/index.html

## API Calls

### Get All Interest rates

```shell
curl --location --request GET 'http://localhost:8080/api/v1/interest-rates' \
--header 'Content-Type: application/json' \
--data-raw '{
    "income": 80000,
    "maturityPeriod": 144,
    "loanValue": 200000,
    "homeValue": 300000

}'
```

### Mortgage Check

```shell
curl --location --request POST 'http://localhost:8080/api/v1/mortgage-check' \
--header 'Content-Type: application/json' \
--data-raw '{
    "income": 80000,
    "maturityPeriod": 144,
    "loanValue": 200000,
    "homeValue": 300000

}'
```