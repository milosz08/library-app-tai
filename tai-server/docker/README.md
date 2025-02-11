# TAI Server

Java (Spring Boot) server image for library management app (TAI).

[GitHub repository](https://github.com/milosz08/library-app-tai)
| [Server app](https://github.com/milosz08/library-app-tai/tree/master/tai-server)

## Build image

```bash
docker build -t milosz08/tai-server .
```

## Create container

* Using command:

```bash
docker run -d \
  --name tai-server \
  -p 9674:9674 \
  -e TAI_PORT=<internal docker port, 9674> \
  -e TAI_DB_HOST=<database host> \
  -e TAI_DB_NAME=<database name> \
  -e TAI_DB_USERNAME=<database username> \
  -e TAI_DB_PASSWORD=<database password> \
  -e TAI_CLIENT_URL=<client url (for CORS)> \
  -e TAI_SERVER_XMS=1024m \
  -e TAI_SERVER_XMX=1024m \
  -e TAI_MAIL_HOST=<SMTP server host> \
  -e TAI_MAIL_PORT=<SMTP server port, 587> \
  -e TAI_MAIL_USERNAME=<SMTP server username, empty> \
  -e TAI_MAIL_PASSWORD=<SMTP server password, empty> \
  -e TAI_MAIL_SSL_ENABLED=<SMTP enable SSL, false> \
  -e TAI_MAIL_AUTH_ENABLED=<SMTP enable Auth, false> \
  milosz08/tai-server:latest
```

* Using `docker-compose.yml` file:

```yaml
services:
  tai-server:
    container_name: tai-server
    image: milosz08/tai-server:latest
    ports:
      - '9674:9674'
    environment:
      TAI_PORT: <internal docker port, 9674>
      TAI_DB_HOST: <database host>
      TAI_DB_NAME: <database name>
      TAI_DB_USERNAME: <database username>
      TAI_DB_PASSWORD: <database password>
      TAI_CLIENT_URL: <client url (for CORS)>
      TAI_SERVER_XMS: 1024m
      TAI_SERVER_XMX: 1024m
      TAI_MAIL_HOST: <SMTP server host>
      TAI_MAIL_PORT: <SMTP server port, 587>
      TAI_MAIL_USERNAME: <SMTP server username, empty>
      TAI_MAIL_PASSWORD: <SMTP server password, empty>
      TAI_MAIL_SSL_ENABLED: <SMTP enable SSL, false>
      TAI_MAIL_AUTH_ENABLED: <SMTP enable Auth, false>
    networks:
      - tai-network

  # other containers...

networks:
  tai-network:
    driver: bridge
```
