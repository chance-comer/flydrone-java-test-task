Список переменных среды для работы:

    FLYDRONE_JAVA_TEST_TASK_DB_PASSWORD
    FLYDRONE_JAVA_TEST_TASK_DB_URL
    FLYDRONE_JAVA_TEST_TASK_DB_USERNAME
    
    AWS_ACCESS_KEY_ID
    AWS_SECRET_ACCESS_KEY

## Профиль

#### Пример GET запроса:

    curl -i --request GET http://localhost:8080/api/profile/36

#### Пример DELETE запроса:

    curl -i --request DELETE http://localhost:8080/api/profile/36

#### Пример POST запроса:
- Вставка (не указан id):

        curl -i --request POST -H "Content-type: application/json" --data '{"lastName":"anikin","firstName":"georg","birthdate":"2005-07-06","patronymic":"igor"}' http://localhost:8080/api/profile

- Обновление:  
    
        curl -i --request POST -H "Content-type: application/json" --data '{"lastName":"anikin","firstName":"georg","birthdate":"2005-07-06","patronymic":"igor","id":36}' http://localhost:8080/api/profile

- Обновление несуществующей записи:  

        curl -i --request POST -H "Content-type: application/json" --data '{"lastName":"anikin","firstName":"georg","birthdate":"2005-07-06","patronymic":"igor","id":3666}' http://localhost:8080/api/profile

- Вставка (не указан id) некорректных данных (возраст меньше 18):  

        curl -i --request POST -H "Content-type: application/json" --data '{"lastName":"anikin","firstName":"georg","birthdate":"2010-07-06"}' http://localhost:8080/api/profile

- Вставка (не указан id) некорректных данных (не указано имя):  

        curl -i --request POST -H "Content-type: application/json" --data '{"lastName":"anikin","birthdate":"2004-07-06"}' http://localhost:8080/api/profile

## Аватар

#### Пример POST запроса:

- Вставка (не указан id):

        curl -i -F avatar=@D:/downloads/avatars.jpg http://localhost:8080/api/avatar/36
