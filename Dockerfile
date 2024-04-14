FROM azul/zulu-openjdk:17
COPY build/libs/people.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
