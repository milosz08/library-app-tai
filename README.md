# Library management app

This monorepo includes library management app build in React SPA (client) and Java Spring Boot (server).

### Services:

| Service name   | Local address                 | Remote address                                           | Docker image                                           |
|----------------|-------------------------------|----------------------------------------------------------|--------------------------------------------------------|
| Client (React) | [9673](http://localhost:9673) | [tai.miloszgilga.pl](https://tai.miloszgilga.pl)         | [source](https://hub.docker.com/r/milosz08/tai-client) |
| Server (Java)  | [9672](http://localhost:9672) | [api.tai.miloszgilga.pl](https://api.tai.miloszgilga.pl) | [source](https://hub.docker.com/r/milosz08/tai-server) |
| PHPMyAdmin     | [9671](http://localhost:9671) | -                                                        | -                                                      |

Postman REST API collection: [link](https://www.postman.com/navigation-architect-44725773/tai/collection/ufvyq5e/tai-rest-api).

## Table of content

* [Prerequisites](#prerequisites)
* [Clone and install](#clone-and-install)
* [Run with Docker (simplest, not for development)](#run-with-docker)
* [Setup client (for development)](#setup-client)
* [Setup server (for development)](#setup-server)
* [Tech stack](#tech-stack)

## Prerequisites

To run client, you must have:
* Free `9673` port at your machine.
* Node (at least v20) - *only for development installation*.
* Yarn (see `setup client` section) - *only for development installation*.

To run server, you must have:
* Free `9670`, `9671` (optionally) and `9672` ports at your machine.
* At least Java 17 - *only for development installation*.
* Docker and docker compose - *only for development installation*.

## Clone and install

To install the program on your computer use the command (or use the built-in GIT system in your IDE environment):

```bash
$ git clone https://github.com/milosz08/library-app-tai
```

## Run with Docker (simplest, not for development)

1. Go to root directory (where file `docker-compose.yml` is located) and type:

```bash
$ docker compose up -d 
```

This command should create 4 docker containers:

| Container name | Port | Description      |
|----------------|------|------------------|
| tai-mysql-db   | 9670 | MySQL database   |
| tai-phpmyadmin | 9671 | Database client  |
| tai-server     | 9672 | Application API  |
| tai-client     | 9673 | React SPA client |

> NOTE: If you have already MySQL db client, you can omit creating `tai-phpmyadmin` container. To omit, create only
> MySQL db container via: `$ docker compose up -d tai-mysql-db tai-server tai-client`.

## Setup client (for development)

1. Go to client directory (`$ cd tai-client`) and install all dependencies via:

```bash
$ yarn install --frozen-lockfile
```
> NOTE: If you do not have yarn, install via: `npm i -g yarn` or use via
> npx command: `npx yarn install --frozen-lockfile`.

2. Activate ESLint server via `Restart ESLint server` command (`Ctrl+Shift+P`). Check output console, if has no errors.
Set default formatter by: `Right click` -> `Format document with` -> `Select default formatter` and select `ESLint`.

3. Run client via:

```bash
$ yarn run dev
```

4. Alternatively to run development server, type:

```bash
$ yarn run preview
```

## Setup server (for development)

1. Go to project root directory and type:

```bash
$ docker compose up -d tai-mysql-db tai-phpmyadmin
```

This command should create 2 docker containers:

| Container name | Port | Description     |
|----------------|------|-----------------|
| tai-mysql-db   | 9670 | MySQL database  |
| tai-phpmyadmin | 9671 | Database client |

> NOTE: If you have already MySQL db client, you can omit creating `tai-phpmyadmin` container. To omit, create only
> MySQL db container via: `$ docker compose up -d tai-mysql-db`.

2. Go to server directory (`$ cd tai-server`) and type (for UNIX):

```
$ ./mvnw clean install
$ ./mvnw spring-boot:run
```

or for Windows:

```
$ mvnw.cmd clean install
$ mvnw.cmd spring-boot:run
```

if you can run project via command line. Alternatively you can run via prepared Intellij run configuration (only for
Intellij Ultimate edition).

3. Check application state via endpoint: [/actuator/health](http://localhost:9672/actuator/health). If response show
this:

```json
{
  "status": "UP"
}
```

application is running and waiting for http requests.

## Tech stack

* React 18,
* Vite,
* Java 17, Spring Boot 3,
* Spring Data JPA, Liquibase, MySQL,
* Docker containers.
