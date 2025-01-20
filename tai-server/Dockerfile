FROM eclipse-temurin:17-jdk-alpine AS build

SHELL ["/bin/sh", "-c"]

ENV BUILD_DIR=/build/tai-server

RUN mkdir -p $BUILD_DIR
WORKDIR $BUILD_DIR

# copy only maven-based resources for optimized caching
COPY .mvn $BUILD_DIR/.mvn
COPY mvnw $BUILD_DIR/mvnw
COPY pom.xml $BUILD_DIR/pom.xml

RUN chmod +x $BUILD_DIR/mvnw
RUN cd $BUILD_DIR

RUN ./mvnw dependency:go-offline

# copy rest of resources
COPY ./src $BUILD_DIR/src
COPY ./docker $BUILD_DIR/docker

RUN ./mvnw clean
RUN ./mvnw package

FROM eclipse-temurin:17-jre-alpine

ENV BUILD_DIR=/build/tai-server
ENV ENTRY_DIR=/app/tai-server
ENV JAR_NAME=tai-server.jar

WORKDIR $ENTRY_DIR

COPY --from=build $BUILD_DIR/target/$JAR_NAME $ENTRY_DIR/$JAR_NAME
COPY --from=build $BUILD_DIR/docker/entrypoint $ENTRY_DIR/entrypoint

RUN sed -i \
  -e "s/\$JAR_NAME/$JAR_NAME/g" \
  entrypoint

RUN chmod +x entrypoint

LABEL maintainer="JWizard <info@jwizard.pl>"

EXPOSE 9672
ENTRYPOINT [ "./entrypoint" ]