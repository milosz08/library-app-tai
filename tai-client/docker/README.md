# TAI Client

React SPA (client) for library management app (TAI).

[GitHub repository](https://github.com/milosz08/library-app-tai)
| [Client app](https://github.com/milosz08/library-app-tai/tree/master/tai-client)

## Build image

```bash
docker build \
  --build-arg TAI_CONTEXT=<vite context (development, production)> \
  -t milosz08/tai-client ./tai-client
```

## Create container

* Using command:

```bash

docker run -d \
  --name tai-client \
  -p 9673:9673 \
  milosz08/tai-client:latest
```

* Using `docker-compose.yml` file:

```yaml
services:
  tai-client:
    container_name: tai-client
    image: milosz08/tai-client:latest
    ports:
      - '9673:9673'
    networks:
      - tai-network

  # other containers...

networks:
  tai-network:
    driver: bridge
```