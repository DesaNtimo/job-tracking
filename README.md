# Job Tracking: REST API для подбора вакансий

**Job Tracking** — backend-сервис для подбора вакансий. Проект демонстрирует переход от консольного приложения к REST API на Spring Boot.

---

## 🚀 Режимы работы

### 1. REST API (основной)

Запуск `JobApplication` поднимает сервер на `http://localhost:8080`.

**Запуск:**
1. Клонировать репозиторий
2. Открыть в IDE
3. Запустить `JobApplication`

---

### 2. Консольный режим (legacy)

Запуск `Main` включает CLI-интерфейс.

---

## 🛠️ Технологии

- Java 17+
- Spring Boot 3.x
- Spring Web
- Embedded Tomcat
- In-memory storage

---

## 📡 REST API

### Пользователи (`/users`)

| Метод | Эндпоинт      | Описание                       | Тело запроса                                     |
|-------|---------------|--------------------------------|--------------------------------------------------|
| POST  | /users        | Создать пользователя           | {"name":"ivan","skills":["java"],"experience":5} |
| GET   | /users        | Получить всех пользователей    | -                                                |
| GET   | /users/{name} | Получить пользователя по имени | -                                                |

---

### Вакансии (`/jobs`)

| Метод | Эндпоинт | Описание                 | Тело запроса                                                               |
|-------|----------|--------------------------|----------------------------------------------------------------------------|
| POST  | /jobs    | Создать вакансию         | {"title":"Java Dev","company":"VK","tags":["java"],"requiredExperience":3} |
| GET   | /jobs    | Получить список вакансий | -                                                                          |

---

### Подбор (`/suggest`)

| Метод | Эндпоинт        | Описание              |
|-------|-----------------|-----------------------|
| GET   | /suggest/{name} | Рекомендации вакансий |

---

### Статистика (`/stat`)

| Метод | Эндпоинт                       | Описание                    |
|-------|--------------------------------|-----------------------------|
| GET   | /stat/top-skills/{n}           | Топ-N навыков               |
| GET   | /stat/jobs-by-experience/{exp} | Вакансии с опытом >= exp    |
| GET   | /stat/users-by-matches/{count} | Пользователи по совпадениям |

---

## 💻 Консольные команды

| Команда                   | Описание              | Пример                                    |
|---------------------------|-----------------------|-------------------------------------------|
| user <name> --skills=...  | Создать пользователя  | user ivan --skills=java --exp=5           |
| user-list                 | Список пользователей  | user-list                                 |
| job <title> --company=... | Создать вакансию      | job Java --company=VK --tags=java --exp=3 |
| job-list                  | Список вакансий       | job-list                                  |
| suggest <name>            | Подбор вакансий       | suggest ivan                              |
| stat --exp <years>        | Фильтр по опыту       | stat --exp 3                              |
| stat --top-skills <count> | Топ навыков           | stat --top-skills 5                       |
| stat --match              | Статистика совпадений | stat --match                              |
| history                   | История запросов      | history                                   |
| exit                      | Выход                 | exit                                      |

---

## 🏗️ Структура проекта

- `controller` — REST-контроллеры
- `service` — бизнес-логика
- `domain` — модели (`User`, `Job`)
- `repository` — in-memory хранилище

---

## 🔮 Улучшения

- Подключение PostgreSQL
- Добавление тестов
- Валидация DTO
- Spring Security
- Swagger/OpenAPI

---

## 🤝 Contribution

1. Fork
2. Branch (`feature/...`)
3. Commit
4. Push
5. Pull Request