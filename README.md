### Разработка
1. Клиент находится вот [тут](https://github.com/kotAndEgor/react-redux).
2. Сервер находится в текущем репозитории

Для начала разработки Вам необходимо:
1. Java 
2. Maven
3. Docker (TestContainers)
4. Node.js (npm)
5. MySql server

Далее:
```sh
git clone https://github.com/kotAndEgor/db-backend (back-end)
cd src/main/resources/client/
git clone https://github.com/kotAndEgor/react-redux (front-end)
```

Для запуска dev hot-reload сборки клиента java-сервер не нужен, запускается webpack-dev-server след. командой:
```sh
npm start
```

Cборка клинта для `ПРОДАКШОНА` =). Java server при запуске смотрит как раз в папку dist на index.html, в которую собирается клиент
```sh
npm run build
```

При запуске JAVA сервера, убедиться что прописали env var и запустили MySql server.
