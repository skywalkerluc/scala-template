FROM sbtscala/scala-sbt:eclipse-temurin-focal-17.0.10_7_1.9.8_2.13.12 as builder

WORKDIR /builder

COPY . /builder

RUN --mount=type=secret,id=GITHUB_TOKEN \
    --mount=type=secret,id=GITHUB_USER \
    export GITHUB_TOKEN=$(cat /run/secrets/GITHUB_TOKEN) && \
    export GITHUB_USER=$(cat /run/secrets/GITHUB_USER) && \
    sbt clean universal:packageZipTarball

RUN tar -xvf /builder/app/target/universal/app-1.0.0.tgz

FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2-jdk

ENV FLYWAY_VERSION=7.15.0

ENV FLYWAY_MIGRATIONS_PATH="adapter/src/main/resources/migration/*.sql"

ENV AWS_MYSQL_JDBC_VERSION=1.1.0

RUN yum install -y wget shadow-utils tar gzip procps-ng && yum clean all

WORKDIR /app

COPY --from=builder /builder/templates ./templates

COPY --from=builder /builder/app-1.0.0 .

COPY cicd/docker/entrypoint.sh .

RUN chmod +x entrypoint.sh

RUN wget -qO- https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/7.15.0/flyway-commandline-${FLYWAY_VERSION}-linux-x64.tar.gz | tar xvz && ln -sf /app/flyway-${FLYWAY_VERSION} /app/flyway \
    && wget -q https://github.com/awslabs/aws-mysql-jdbc/releases/download/${AWS_MYSQL_JDBC_VERSION}/aws-mysql-jdbc-${AWS_MYSQL_JDBC_VERSION}.jar -O /app/flyway-${FLYWAY_VERSION}/drivers/aws-mysql-jdbc.jar

COPY ${FLYWAY_MIGRATIONS_PATH} /app/flyway-${FLYWAY_VERSION}/sql/

RUN groupadd -r app && useradd --no-log-init -d /app -s /usr/sbin/nologin -r -g app app && chown -R app:app /app

USER app

ENTRYPOINT ["./entrypoint.sh"]

CMD ["web"]
