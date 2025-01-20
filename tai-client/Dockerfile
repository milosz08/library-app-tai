FROM node:20-alpine AS build

# vite build context, development or production
ARG TAI_CONTEXT

ENV BUILD_DIR=/build/tai-client

RUN mkdir -p $BUILD_DIR
WORKDIR $BUILD_DIR

COPY /package.json $BUILD_DIR/package.json
COPY /yarn.lock $BUILD_DIR/yarn.lock

RUN yarn install --frozen-lockfile

# copy rest of content
COPY . $BUILD_DIR/

RUN yarn run build -- --mode $TAI_CONTEXT

FROM caddy:latest

ENV BUILD_DIR=/build/tai-client
ENV ENTRY_DIR=/app/tai-client

COPY --from=build $BUILD_DIR/dist/ $ENTRY_DIR
COPY --from=build $BUILD_DIR/docker/Caddyfile /etc/caddy/Caddyfile

LABEL maintainer="Miłosz Gilga <personal@miloszgilga.pl>"

EXPOSE 9673
