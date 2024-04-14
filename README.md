Проект представляет собой простое приложение для генерации NPC (персонажей в играх, которые не управляются игроком).

### Функционал приложения ###
1. Генерация профиля NPC
2. Создание аватара
3. Создания цитаты
4. Сохранение в бд (postgres)

### API приложения ###
- http://localhost:8080/me - вывод информации о текущем пользователе
- http://localhost:8080/generate - добавление нового NPC
- http://localhost:8080/id/{id} - получение сгенерированного NPC по id

### Запуск приложения ###
- запустить можно через sh/bat-скрипты в директории scripts
- либо с помощью gradle task: dockerComposeUp
