# Library management app

This monorepo includes library management app build in React SPA (client) and Java Spring Boot (
server).

### Services:

| Service name       | Local address                 | Remote address                                   | Docker image                                                |
|--------------------|-------------------------------|--------------------------------------------------|-------------------------------------------------------------|
| App (Java + React) | [9674](http://localhost:9674) | [tai.miloszgilga.pl](https://tai.miloszgilga.pl) | [source](https://hub.docker.com/r/milosz08/library-app-tai) |
| Mailhog UI         | [9673](http://localhost:9673) | -                                                | -                                                           |
| PHPMyAdmin         | [9671](http://localhost:9671) | -                                                | -                                                           |

Postman REST API
collection: [link](https://www.postman.com/navigation-architect-44725773/tai/collection/ufvyq5e/tai-rest-api).

## Table of content

* [Clone and install](#clone-and-install)
* [Run with Docker (simplest, not for development)](#run-with-docker-simplest-not-for-development)
* [Setup (for development)](#setup-for-development)
* [Create executable JAR file (bare-metal)](#create-executable-jar-file-bare-metal)
* [Tech stack](#tech-stack)

## Clone and install

To install the program on your computer use the command (or use the built-in GIT system in your IDE
environment):

```bash
$ git clone https://github.com/milosz08/library-app-tai
```

## Run with Docker (simplest, not for development)

1. Go to root directory (where file `docker-compose.yml` is located) and type:

```bash
$ docker compose up -d
```

This command should create 4 docker containers:

| Container name | Port(s)    | Description                |
|----------------|------------|----------------------------|
| tai-mysql-db   | 9670       | MySQL database             |
| tai-phpmyadmin | 9671       | Database client            |
| tai-mailhog    | 9672, 9673 | Mailhog client and server  |
| tai-app        | 9674       | Application (Java + React) |

> NOTE: If you have already MySQL db client, you can omit creating `tai-phpmyadmin` container. To
> omit, create only MySQL db container
> via: `$ docker compose up -d tai-mysql-db tai-mailhog tai-server tai-client`.

## Setup (for development)

1. Setup client:

* Go to client directory (`$ cd tai-client`) and install all dependencies via:

```bash
$ yarn install --frozen-lockfile
```

> NOTE: If you do not have yarn, install via: `npm i -g yarn`.

* Run client via:

```bash
$ yarn run dev
```

* Alternatively to run development server, type:

```bash
$ yarn run preview
```

Development server should be available at `9675` TCP port.

2. Setup server:

* Go to project root directory and type:

```bash
$ docker compose up -d tai-mysql-db tai-phpmyadmin tai-mailhog
```

This command should create 2 docker containers:

| Container name | Port(s)    | Description               |
|----------------|------------|---------------------------|
| tai-mysql-db   | 9670       | MySQL database            |
| tai-phpmyadmin | 9671       | Database client           |
| tai-mailhog    | 9672, 9673 | Mailhog client and server |

> NOTE: If you have already MySQL db client, you can omit creating `tai-phpmyadmin` container. To
> omit, create only
> MySQL db container via: `$ docker compose up -d tai-mysql-db tai-mailhog`.

* Go to server directory (`$ cd tai-server`) and type (for UNIX):

```
$ ./mvnw clean install
$ ./mvnw spring-boot:run
```

or for Windows:

```
$ mvnw.cmd clean install
$ mvnw.cmd spring-boot:run
```

* Check application state via endpoint: [/actuator/health](http://localhost:9674/actuator/health).
  If response show
  this:

```json
{
  "status": "UP"
}
```

application is running and waiting for http requests.

## Create executable JAR file (bare-metal)

To create executable JAR file (client + server), you must type (for UNIX):

```bash
$ ./mvnw clean package
```

or for Windows:

```bash
.\mvnw.cmd clean package
```

Output JAR file will be located inside `.bin` directory. With this file you can run app in
bare-metal environment without virtualization via:

```bash
$ java \
  -Xms1024m \
  -Xmx1024m \
  -Dspring.profiles.active=prod \
  -Dserver.port=8080 \
  -jar library-app-tai.jar
```

## Tech stack

* React 18, Vite,
* Java 17, Spring Boot 3,
* Spring Data JPA, Liquibase, MySQL,
* Mailhog, JavaMail, Thymeleaf (mail templates),
* Docker containers.
