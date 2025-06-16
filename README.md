# Dremio REST Server

[![license](https://img.shields.io/badge/license-Apache%202.0-blue)](https://github.com/scc-digitalhub/dremio-rest-server/LICENSE) ![GitHub Release](https://img.shields.io/github/v/release/scc-digitalhub/dremio-rest-server)
![Status](https://img.shields.io/badge/status-stable-gold)

The Dremio REST Server is a Java SpringBoot application that provides a REST API for Dremio using Arrow Flight JDBC driver.

**NOTE**: Parametrized queries are not currently supported by Flight JDBC driver.

Explore the full documentation at the [link](https://scc-digitalhub.github.io/docs/).

## Quick start

You can download and run the docker image from GHCR:

```sh
docker pull ghcr.io/scc-digitalhub/dremio-rest-server:0.0.9
docker run -p 8080:8080 -e "DREMIO_TABLES=myspace.mytable1,myspace.mytable2" dremio-rest-server 
```

## Configuration

You can configure the application via environment variables. The following variables are supported (see ["application.yaml"](https://github.com/scc-digitalhub/dremio-rest-server/blob/main/src/main/resources/application.yaml) file for defaults):

| KEY           | DESCRIPTION                                                            |
| ------------- | ---------------------------------------------------------------------- |
| DREMIO_URL    | The URL to Dremio Arrow Flight service, with credentials               |
| DREMIO_TABLES | Comma-separated list of Dremio tables that the application will expose |

## Development

See CONTRIBUTING for contribution instructions.

### Build from source

The application requires Java 11.

Clone the repository with `git clone https://github.com/scc-digitalhub/dremio-rest-server.git`.

Build with `./mvnw clean install`.

Start with `_JAVA_OPTIONS="--add-opens=java.base/java.nio=ALL-UNNAMED" ./mvnw spring-boot:run` (see https://arrow.apache.org/docs/java/install.html#java-compatibility).

To start with a local profile, use `_JAVA_OPTIONS="--add-opens=java.base/java.nio=ALL-UNNAMED" ./mvnw spring-boot:run -Dspring-boot.run.profiles=local`.

### Build container images

The Dockerfile is included in the repository.

```sh
cd dremio-rest-server
docker build . -t dremio-rest-server:0.0.9
```

## Security Policy

The current release is the supported version. Security fixes are released together with all other fixes in each new release.

If you discover a security vulnerability in this project, please do not open a public issue.

Instead, report it privately by emailing us at digitalhub@fbk.eu. Include as much detail as possible to help us understand and address the issue quickly and responsibly.

## Contributing

To report a bug or request a feature, please first check the existing issues to avoid duplicates. If none exist, open a new issue with a clear title and a detailed description, including any steps to reproduce if it's a bug.

To contribute code, start by forking the repository. Clone your fork locally and create a new branch for your changes. Make sure your commits follow the [Conventional Commits v1.0](https://www.conventionalcommits.org/en/v1.0.0/) specification to keep history readable and consistent.

Once your changes are ready, push your branch to your fork and open a pull request against the main branch. Be sure to include a summary of what you changed and why. If your pull request addresses an issue, mention it in the description (e.g., “Closes #123”).

Please note that new contributors may be asked to sign a Contributor License Agreement (CLA) before their pull requests can be merged. This helps us ensure compliance with open source licensing standards.

We appreciate contributions and help in improving the project!

## Authors

This project is developed and maintained by **DSLab – Fondazione Bruno Kessler**, with contributions from the open source community. A complete list of contributors is available in the project’s commit history and pull requests.

For questions or inquiries, please contact: [digitalhub@fbk.eu](mailto:digitalhub@fbk.eu)

## Copyright and license

Copyright © 2025 DSLab – Fondazione Bruno Kessler and individual contributors.

This project is licensed under the Apache License, Version 2.0.
You may not use this file except in compliance with the License. Ownership of contributions remains with the original authors and is governed by the terms of the Apache 2.0 License, including the requirement to grant a license to the project.
