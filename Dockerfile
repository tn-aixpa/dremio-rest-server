# SPDX-FileCopyrightText: © 2025 DSLab - Fondazione Bruno Kessler
#
# SPDX-License-Identifier: Apache-2.0

FROM maven:3.8.2-eclipse-temurin-11 AS build
COPY ./src /tmp/src
COPY ./pom.xml /tmp/pom.xml
WORKDIR /tmp
RUN --mount=type=cache,target=/root/.m2,source=/root/.m2,from=ghcr.io/scc-digitalhub/dremio-rest-server:cache \ 
    mvn package -DskipTests

FROM gcr.io/distroless/java11-debian11:nonroot
ENV APP=dremio-rest-server-0.0.1-SNAPSHOT.jar
LABEL org.opencontainers.image.source=https://github.com/scc-digitalhub/dremio-rest-server
COPY --from=build /tmp/target/*.jar /app/${APP}
EXPOSE 8080
CMD ["/app/dremio-rest-server-0.0.1-SNAPSHOT.jar", "--add-opens=java.base/java.nio=ALL-UNNAMED"]