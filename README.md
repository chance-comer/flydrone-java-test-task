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

- Вставка профиля с аватаром

        curl -i --request POST -H "Content-Type: multipart/form-data" -F "avatar=@D:/downloads/avatars.jpg" -F "userprofile={\"lastName\":\"puskin\",\"firstName\":\"alex\",\"birthdate\":\"1950-07-06\"};type=application/json" http://localhost:8080/api/fullprofile

- Обновление профиля с аватаром

        curl -i --request POST -H "Content-Type: multipart/form-data" -F "avatar=@D:/downloads/avatars.jpg" -F "userprofile={\"lastName\":\"puskin\",\"firstName\":\"alex\",\"birthdate\":\"1950-07-06\",\"id\":\"42\"};type=application/json" http://localhost:8080/api/fullprofile

## Аватар

#### Пример POST запроса:

- Вставка или обновление аватара несуществующего пользователя:

        curl -i --request POST -F avatar=@D:/downloads/avatars.jpg http://localhost:8080/api/avatar/36

- Вставка или обновление аватара с превышением максимального размера файла (более 2 Мб)

        curl -i --request POST -F avatar=@D:/downloads/large_avatar.jpg http://localhost:8080/api/avatar/12      

- Вставка или обновление аватара с типом, отличным от PNG и JPG

        curl -i --request POST -F avatar=@D:/downloads/gif_avatar.gif http://localhost:8080/api/avatar/12 

- Корректная вставка или обновление аватара

        curl -i --request POST -F avatar=@D:/downloads/avatars.jpg http://localhost:8080/api/avatar/12 

#### Пример GET запроса:

- При отсутствии пользователя

        curl --request GET http://localhost:8080/api/avatar/36 --output "D:/downloads/avatar"

- При отсутствии аватара у существующего пользователя

        curl --request GET http://localhost:8080/api/avatar/14 --output "D:/downloads/avatar"

- При наличии и пользователя, и аватара

        curl --request GET http://localhost:8080/api/avatar/12 --output "D:/downloads/avatar"

#### Пример DELETE запроса:
   
- При наличии пользователя

        curl -i --request DELETE http://localhost:8080/api/avatar/12

- При отсутствии пользователя

        curl -i --request DELETE http://localhost:8080/api/avatar/36
