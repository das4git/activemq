ActiveMQ 
Postgresql

Реализовано отправление сообщений и их получение через MessageListener.
Отдельные потоки для отправителя и получателя.
Создана БД с двумя таблицами.
В таблицу 1 отправляется тело сообщения.
В таблицу 2 отправлется заголовок сообщения. 
Связь между таблицами через внешний ключ.
Запуск через Spring (XML конфигурация).
