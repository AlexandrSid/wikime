# wikime
Long project of Java Enterprice Developer course
Eng version below.

Назначение приложения - записная книга для небольшой комманды, задумывалось как решение для сохранения и передачи знаний и инструкций на работе, 
так как корпоративная knowledge base слишком громоздкая и медленно редактируемая. Должно будет цеплять любую совместимую базу данных, содержащую любые тематические данные в виде html страниц
У пользователей можно будет выставлять уровень доступа к данным (админ, модератор, читатель).
База и приложение буду работать из разных докер контейнеров.

Приложение строится в рамках курса Java Enterprice Developer от itmentor.ru.
План его реализации примерно таков: Servlet+JSP - JDBC - Hiberntale - JUnit - Spring (core-MVC-security-test-data) - Docker - Logging - CICD piplene - Kafka - REST + Mongo (немножко микросервис)

Далее только написание недостающего функционала, рефакторинг и рабочее развёртывание.

Версия с базовым функционалом развёрнута в google.cloud через github CICD pipeline и доступна по адресу http://34.116.143.189/

this ReadMe will be updated during project changes

Current vertion 13.07.20201, Readme updated Sept. 7th



This application was created as knowlage base for little team so far the corporate one is overloaded with functionality and hard to use.

This application developed as part of study plan of Java Enterprice Developer cource from https://stc.innopolis.university/enterprise-java-developer#program

There are pack of old and actual technologies used in it: Servlet+JSP - JDBC - Hiberntale - JUnit - Spring (core-MVC-security-test-data) - Docker - Logging - CICD piplene - Kafka - REST + Mongo (microservice architecture)

Base version of application was deployed to google.cloud with github's CICD pipeline and available here:http://34.116.143.189/
