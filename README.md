# Task-Manager — учебный проект в микросервисной Event-Driven архитектуре.
Стек: Java 21, Spring Boot 3.4.11, Spring Cloud 2024.0.2, OpenFeign, MapStruct, Lombok, Springdoc OpenAPI, Docker + Docker Compose, Grafana + Prometheus

Планируется: 
  - Мониторинг сервисов с помощью Actuator + Micrometer + Prometheus + Tempo + Loki;
  - Аутентификация и авторизация с Keycloak.
  - Автоматизация через Makefile 

> Репозиторий демонстрирует навыки в проектировании и реализации масштабируемых систем: событийно-ориентированное взаимодействие сервисов, хранение персональных данных и базовая наблюдаемость (метрики и трассировки).


## Архитектура
<img width="1714" height="804" alt="task-manager" src="https://github.com/user-attachments/assets/6e401447-5d46-4b3b-9d8c-d58d54429264" />

## Состав репозитория
```
task-manager-micro/
├── prometheus/
│   └── prometheus.yaml                   # Конфигурация prometheus
├── api-gateway/
│   ├── src/main/resources/
│   │   └── application.properties        # Конфигурация api-gateway
│   └── Dockerfile
├── eureka-server/
│   ├── src/main/resources/
│   │   └── application.properties        # Конфигурация eureka-server
│   └── Dockerfile
├── task-service/
│   ├── src/main/resources/static/
│   │   └── openapi.yaml                   # Контракт для генерации DTO и контроллеров
│   └── Dockerfile
├── user-service/
│   ├── src/main/resources/static/
│   │   └── openapi.yaml                   # Контракт для генерации DTO и контроллеров
│   └── Dockerfile
├── docker-compose.yaml
└── .env
```

## Быстрый запуск
### Makefile

> В процессе разработки

### Docker
```
docker-compose up --build
```

## Порты и сервисы
| Сервис | Порт | URL | Назначение | Дополнительно |
|--------|------|-----|------------|---------------|
| User Service | 8081 | http://localhost:8081 | Основной сервис пользователей | Swagger `/swagger-ui/index.html` |
| Task Service | 8082 | http://localhost:8082 | Основной сервис задач | Swagger `/swagger-ui/index.html` |
| Eureka Server | 8761 | http://localhost:8761 | Service Discovery | Has a web-client |
| Api Gateway | 8080 | http://localhost:8080 | Единая точка входа | Example `/task-service/api/v1/tasks/hello` |
| Postgres (user-db) | 5432 | - | Основная БД пользователей | - |
| Postgres (task-db) | 5433 | - | Основная БД задач | - |
| Kafka | 29092/9092 | - | Брокер сообщений | - |
| Kafka-UI | 8070 | http://localhost:8070 | Мониторинг Kafka | Has a web-client |
| Prometheus | 9090 | http://localhost:9090 | Метрики | Has a web-client |
| Grafana | 3000 | http://localhost:3000 | Дашборды | Has a web-client |

## API
### User service
> В процессе написания

### Task service
> В процессе написания
