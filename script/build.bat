cd ..
call gradlew bootJar
call docker build . -t people
