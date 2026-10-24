## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/

Список выполненных задач:

### 2. Удалены социальные сети VK и Yandex

- удалены обработчики `VkOAuth2UserDataHandler` и `YandexOAuth2UserDataHandler`;
- из `application.yaml` убраны настройки OAuth2 для vk и yandex (`registration` и `provider`);
- со страниц входа и регистрации (`login.html`, `register.html`) убраны кнопки VK и Yandex.

Вход и регистрация через Google, GitHub и GitLab остались. Тип контакта `vk` в профиле пользователя не трогал: это поле контактов, а не вход через соцсеть.
