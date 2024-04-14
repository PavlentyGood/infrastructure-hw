cd ..
call gradlew build
call docker build . -t people
