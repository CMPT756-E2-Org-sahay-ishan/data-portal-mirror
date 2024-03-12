# HALLO data portal backend

The goal of this project is to create a platform for HALLO - Humans and Algorithms Listening to Orcas. to support researchers, citizen scientist, the public and protect marine wildlife in the Salish Sea. 

At this stage of the project, our focus is on collecting a variety of data streams and make them accessible to researchers and citizen scientists. 


## Environment Setup

### Configure usernames, passwords, and other sensitive information
  
Sensitive information, like usernames, credentials and file paths, are not hardcoded in source code. Instead, they are parametrized and loaded from environment variables (.env) or properties files (*.properties). [Samples](../sample_config) of `*.properties` and `.env` file are provided.

Whether using the Spring Tools Suite, Maven `mvn` package manager or docker-compose, the hallo-backend expects a root folder structure as follows
```shell
hallo-backend
├─── ...
└─── config
      └─ *.properties
└─── src
└─── .env
└─── pom.xml
```
The variables set in the environment and local `config/` files are automatically loaded when building/loading with Spring Tools Suite and Maven `mvn` package manager. These files will be [ignored](../.dockerignore) during a docker build from the parent folder (e.g. using docker compose).

The contents of an `.env` file can be substituted (copy/paste) in Sprint Tools Suite 4 (or Eclipse) under Run -> Run Configurations... -> Environment tab. Runtime environment variables will override `application-*.properties`.

## Deployment

1. Kill the existing java process.
```shell
ps aux; kill -9 [process id of java -jar target/hallo]
```
2. Change directory into the backend code with `cd hallo-data-portal/hallo-app-backend`. Build the application with `mvn` and run it in headless mode with `nohup`
```shell
mvn clean install -DskipTests

nohup java -jar target/hallo-0.0.1-SNAPSHOT.jar  >/dev/null &
```