Start with `_JAVA_OPTIONS="--add-opens=java.base/java.nio=ALL-UNNAMED" ./mvnw spring-boot:run` (see https://arrow.apache.org/docs/java/install.html#java-compatibility).

To start with a local profile, use `_JAVA_OPTIONS="--add-opens=java.base/java.nio=ALL-UNNAMED" ./mvnw spring-boot:run -Dspring-boot.run.profiles=local`.

**NOTE**: Parametrized queries are not currently supported by Flight JDBC driver.