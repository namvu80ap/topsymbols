# topsymbols Project

## Prerequisites

1. Java 16 or greater
2. Docker 19.3 or greater
3. Gradle 6 or greater

## Prepare .env file
You have to provide the apikey in .env file by open .env and input your key.
```shell script
### .env
BINANCE_SECRET_APIKEY= ###INPUT YOUR APIKEY HERE###
```

## Run full stack with docker
With docker-compose
```shell script
# 1/ Build: On root of the project
./gradlew quarkusBuild
# 2/ Run:
docker-compose -f ./src/main/docker/docker-compose.yml up
```
Or docker stack
```shell script
# 1/ Build: On root of the project
> ./gradlew quarkusBuild
> docker build -f ./src/main/docker/Dockerfile.jvm -t topsymbols .
# 2/ Run:
docker stack deploy --compose-file ./src/main/docker/docker-compose.yml stack
```
## Answer the questions
Use your web browsers to get the result

Question 1 : http://localhost:8080/app/topQuoteVolume

Question 2 : http://localhost:8080/app/topTradeCount

Question 3 : http://localhost:8080/app/topNotionalValue

Question 4 : http://localhost:8080/app/topTradePriceSpread

Question 5 : You can use you wed browsers or httpie
1. http://localhost:8080/index.html  (Wait for 10 seconds)
2. http http://localhost:8080/app/streaming --stream

Question 6:
1. Prometheus Metrics format : http://localhost:8080/metrics
2. Prometheus graph confirm :  http://localhost:9090/graph  (Select prefix topsymbol_price_spread_ )

## Running the application in dev mode
You can run your application in dev or test mode that enables live coding using:
```shell script
./gradlew quarkusDev
## Or
./gradlew quarkusTest
```

> **_NOTE:_**  Dev and Test mode use in-memory database H2

## Run unit test
You can run unit test before build the application
```shell script
./gradlew test
```



