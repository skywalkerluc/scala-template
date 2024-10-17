# project-name-default

Project Name Default

## Requirements (local)

- sbt v1.10.1
- JDK 17 or higher
- scala 2.13.14

### How to set up the project in your computer
Run `sdk env install` or `sdk env`.
It installs the SDKs listed in the .sdkmanrc file, such as java, scala, and sbt,
with the versions required for this project.

## How to run

- Initialize .env file, you can copy from `.local.env.sample`
- docker-compose up -d
- sbt runApp

## Run coverage

Either run:
- `sbt clean coverage test`
- `sbt coverageAggregate`

Or, the following command will run both steps:
- `make tests-coverage`

### API documentation

**Swagger UI:** The microservice uses Swagger for automatic generation
API documentation. To access the Swagger UI, follow these steps:

1. Make sure the microservice is running
2. Open a web browser and navigate to the following URL:
   [http://0.0.0.0:9000/project-name-default/docs](http://0.0.0.0:9000/project-name-default/docs)
3. You will see the Swagger UI where you can explore and interact with API endpoints. This provides a user-friendly interface
   to test your endpoints and understand available API operations


## Troubleshooting

- Got Docker error when running repository tests:
    - If you are using Rancher Desktop, you need to make sure that the container engine is in "dockerd" and use the following command:
      `sudo ln -s $HOME/.rd/docker.sock /var/run/docker.sock` or use Rancher Desktop with administrative access
- When you get the error `NoSuchMethodError: [theMissingMethod]`:
    - Double-check your dependencies using `make dependency-tree` to make sure you have all required dependencies.

