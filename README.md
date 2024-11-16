# Microservice-web-library

--------------------
## Предварительные требования:

* Java 17 или выше
* Maven для сборки проектов
* PostgreSQL для работы с базой данных
* Git для клонирования репозитория
--------------------
# Установка:

### Клонируйте репозиторий:
```bash
git clone https://github.com/timofeibratskov/microservice-web-library/tree/dev
```
### Перейдите в каталог проекта:
```bash
cd microservice-web-library
```
### установите зависимости:
```bash
mvn install
```
--------------------

# Настройка бд:
## Создайте базу данных в PostgreSQL:
```postgresql
create database lib_db 
       
```
#### все sql файлы находятся в папке SQL_TABLES
```bash
psql -U ваш_пользователь -d lib_db -f path/to/users.sql
psql -U ваш_пользователь -d lib_db -f path/to/roles.sql
psql -U ваш_пользователь -d lib_db -f path/to/books.sql
psql -U ваш_пользователь -d lib_db -f path/to/book_status_entity.sql
psql -U ваш_пользователь -d lib_db -f path/to/users_roles.sql
```
 book/src/main/resources/application.yml
```yaml
spring:
  application:
    name: book
  config:
    import: optional:configserver:http://localhost:8888  
  datasource:
    url: jdbc:postgresql://localhost:5432/lib_db   
    username: postgres # ваше имя
    password: 1111 # ваш пароль
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
server:
  port: 8090
```
config-server/src/main/resources/configurations/book.yml
```yaml
spring:
  application:
    name: book

  config:
    import: optional:configserver:http://localhost:8888

  datasource:
    url: jdbc:postgresql://localhost:5432/lib_db
    username: postgres #имя
    password: 1111 # имя
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

server:
  port: 8090

```
config-server/src/main/resources/configurations/lib.yml
``` yaml
spring:
  application:
    name: lib
  datasource:
    url: jdbc:postgresql://localhost:5432/lib_db
    username: postgres  #имя
    password: 1111  #ваш пароль
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
server:
  port: 8070
```
config-server/src/main/resources/configurations/user.yml
```yaml
spring:
  application:
    name: user

  config:
    import: optional:configserver:http://localhost:8888

  datasource:
    url: jdbc:postgresql://localhost:5432/lib_db
    username: postgres  #имя
    password: 1111 #пароль
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

server:
  port: 8081

jwt:
  secret: "suppupkey123123123123"

 ```
в lib/src/main/resources/application.yml
```yaml
spring:
  application:
    name: lib
  config:
    import: optional:configserver:http://localhost:8888  
  datasource:
    url: jdbc:postgresql://localhost:5432/lib_db
    username: postgres #имя
    password: 1111 #пароль
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
server:
  port: 8070
 ```
user/src/main/resources/application.yml
```yaml
spring:
  application:
    name: user
  config:
    import: optional:configserver:http://localhost:8888  
  datasource:
    url: jdbc:postgresql://localhost:5432/lib_db
    username: postgres # имя
    password: 1111 # пароль
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
server:
  port: 8005
```

# Запуск микросервисов

--------------------
### config-server
```bash
cd config-server
```
Выполните команды для запуска
```bash
mvn clean install
mvn spring-boot:run
```
--------------------
### discovery
```bash
cd discovery
```
Выполните команды для запуска
```bash
mvn clean install
mvn spring-boot:run
```
--------------------
### gateway
```bash
cd gateway
```
Выполните команды для запуска
```bash
mvn clean install
mvn spring-boot:run
```
--------------------
### user
```bash
cd user
```
Выполните команды для запуска
```bash
mvn clean install
mvn spring-boot:run
```
--------------------
### book
```bash
cd book
```
Выполните команды для запуска
```bash
mvn clean install
mvn spring-boot:run
```
--------------------
### lib
```bash
cd lib
```
Выполните команды для запуска
```bash
mvn clean install
mvn spring-boot:run
```
--------------------
## swagger доступен по следующим адресам:
* ***UserService: http://localhost:8081/swagger-ui/index.html***
* ***LibraryService: http://localhost:8070/swagger-ui/index.html***
* ***BookService: http://localhost:8090/swagger-ui/index.html***
        
