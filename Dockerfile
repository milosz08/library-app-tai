FROM eclipse-temurin:17-jdk-alpine AS build

ENV BUILD_DIR=/build/library-app-tai

RUN mkdir -p $BUILD_DIR
WORKDIR $BUILD_DIR

# copy only maven-based resources for optimized caching
COPY .mvn $BUILD_DIR/.mvn
COPY mvnw $BUILD_DIR/mvnw
COPY pom.xml $BUILD_DIR/pom.xml
COPY tai-client/pom.xml $BUILD_DIR/tai-client/pom.xml
COPY tai-server/pom.xml $BUILD_DIR/tai-server/pom.xml

RUN chmod +x $BUILD_DIR/mvnw
RUN cd $BUILD_DIR

RUN ./mvnw dependency:tree

# copy rest of resources
COPY tai-client $BUILD_DIR/tai-client
COPY tai-server $BUILD_DIR/tai-server
COPY docker $BUILD_DIR/docker

RUN ./mvnw clean
RUN ./mvnw package

FROM eclipse-temurin:17-jre-alpine

ENV BUILD_DIR=/build/library-app-tai
ENV ENTRY_DIR=/app/library-app-tai
ENV JAR_NAME=library-app-tai.jar

WORKDIR $ENTRY_DIR

COPY --from=build $BUILD_DIR/.bin/$JAR_NAME $ENTRY_DIR/$JAR_NAME
COPY --from=build $BUILD_DIR/docker/entrypoint $ENTRY_DIR/entrypoint

RUN sed -i \
  -e "s/\$JAR_NAME/$JAR_NAME/g" \
  entrypoint

RUN chmod +x entrypoint

LABEL maintainer="Miłosz Gilga <miloszgilga@gmail.pl>"

EXPOSE 8080
ENTRYPOINT [ "./entrypoint" ]
