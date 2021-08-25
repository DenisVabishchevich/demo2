### HOW TO RUN
```zsh
./gradlew clean install 
./gradlew simple:bootRun
```
### Make requests
1. Success url
```zsh
curl -X GET --location "http://localhost:8080/api/v1/data" \
    -H "Idempotency-Key: '6f32cab3-1235-437d-84d6-a22a0832a8f1'"
```
2. Error url
```zsh
curl -X GET --location "http://localhost:8080/api/v1/errors" \
    -H "Idempotency-Key: '6f32cab3-1235-437d-84d6-a22a0832a8f2'"
```