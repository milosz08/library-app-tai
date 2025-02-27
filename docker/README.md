# Library app TAI

Library management app written in Java with Spring Boot (server) and React (client).

[GitHub repository](https://github.com/milosz08/library-app-tai)

## Build image

```bash
docker build -t milosz08/library-app-tai .
```

## Create container

* Using command:

```bash
docker run -d \
  --name tai-app \
  -p 8080:8080 \
  -e TAI_DB_HOST=<database host> \
  -e TAI_DB_NAME=<database name> \
  -e TAI_DB_USERNAME=<database username> \
  -e TAI_DB_PASSWORD=<database password> \
  -e TAI_SERVER_XMS=1024m \
  -e TAI_SERVER_XMX=1024m \
  -e TAI_MAIL_PROTOCOL=<mail protocol, smtps> \
  -e TAI_MAIL_HOST=<SMTP server host> \
  -e TAI_MAIL_PORT=<SMTP server port, 587> \
  -e TAI_MAIL_USERNAME=<SMTP server username, empty> \
  -e TAI_MAIL_PASSWORD=<SMTP server password, empty> \
  -e TAI_MAIL_SSL_ENABLED=<SMTP enable SSL, false> \
  -e TAI_MAIL_AUTH_ENABLED=<SMTP enable Auth, false> \
  -e TAI_MAIL_FROM_ADDRESS=<SMTP sender address> \
  milosz08/library-app-tai:latest
```

* Using `docker-compose.yml` file:

```yaml
services:
  tai-app:
    container_name: tai-app
    image: milosz08/library-app-tai:latest
    ports:
      - '8080:8080'
    environment:
      TAI_DB_HOST: <database host>
      TAI_DB_NAME: <database name>
      TAI_DB_USERNAME: <database username>
      TAI_DB_PASSWORD: <database password>
      TAI_SERVER_XMS: 1024m
      TAI_SERVER_XMX: 1024m
      TAI_MAIL_PROTOCOL: <mail protocol, smtps>
      TAI_MAIL_HOST: <SMTP server host>
      TAI_MAIL_PORT: <SMTP server port, 587>
      TAI_MAIL_USERNAME: <SMTP server username, empty>
      TAI_MAIL_PASSWORD: <SMTP server password, empty>
      TAI_MAIL_SSL_ENABLED: <SMTP enable SSL, false>
      TAI_MAIL_AUTH_ENABLED: <SMTP enable Auth, false>
      TAI_MAIL_FROM_ADDRESS: <SMTP sender address>
    networks:
      - tai-network

  # other containers...

networks:
  tai-network:
    driver: bridge
```
