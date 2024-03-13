# HALLO Data Portal

The goal of this project is to create a platform for HALLO - Humans and Algorithms Listening to Orcas (and the coastal-science organization) to support researchers, citizen scientist, the public and protect marine wildlife in the Salish Sea. The data portal is available at https://portal.orca.research.sfu.ca.

At this stage of the project, our focus is on collecting a variety of data streams and make them accessible to researchers and citizen scientists. 

This work was completed in part with resources provided by the Research Computing Group, IT Services at Simon Fraser University, including the use of the Cedar / Cedar Cloud and the advanced support from the Research Computing Group, to carry out the development presented here.

# Table of Contents
1. [Getting Started](#getting-started)
2. [Database](#database)
3. Standalone [Backend](./hallo-app-backend/README.md "standalone backend") application
4. Standalone [Frontend](./hallo-app-frontend/README.md "standalone frontend") application
5. Standalone [data-pipelines](./data-pipelines/README.md "standalone pipelines")
6. [Docker](#docker)

<!-- ## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors. -->

<!-- ## TODO: Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method. -->

<!-- ## TODO: Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection. -->

## Support
Please, use the issue tracker for discussions about the data portal. Mention other people if you want to use their expertise, have them provide an estimate, or just be aware of an issue. GitHub lets you mention users and entire teams with the @username syntax, much like Twitter and Facebook do. When you add mentions, consider the following:

<!-- ## TODO: Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README. -->

## Contributing
We love to receive feedback from our community — you! There are many ways to contribute, from writing tutorials or blog posts, improving the documentation, submitting bug reports and feature requests.

We look forward to opening this project to community development once the project has matured. At this moment, we are not looking for help writing code to be incorporated into codebase itself. At the moment, your ideas for improvement are welcomed.

- Since every mention results in an email notification to the person you’re mentioning, try to mention sparingly.
- When mentioning a user who was previously not involved in an issue, try to make it clear if there’s an action you want them to take or if you’re just making them aware of what’s going on.

## Authors and acknowledgment
- HALLO Team / coastal-science
    - Ruth Joy (PI)
- Research Computing Group at Simon Fraser University

This work was completed in part with resources provided by the Research Computing Group, IT Services at Simon Fraser University, including the use of the Cedar / Cedar Cloud and the advanced support from the Research Computing Group, to carry out the development presented here.

<!-- ## TODO: License TBD
For open source projects, say how it is licensed. -->

## Getting started

The data portal application consists of loosely connected components including a database, backend server, frontend application and data ingestion pipelines for the variety of data sources.

This project can be deployed using package managers (`mvn`, `npm`, `pip`), IDE and their plugins (Spring Tools Suite, VS Code) or using containers built with `docker-compose`.

### Configure your credentials and secrets
  
Sensitive information, like usernames, credentials and file paths, are not hardcoded in source code. Instead, they are parametrized and loaded from environment variables (.env) or properties files (*.properties) at runtime. [Samples](sample_config/) of `*.properties` and `.env` file are provided.

1. Create a copy of the folder `sample_config` as `config`. 
2. Remove `.sample` from any file names contained in the config folder
3. Fill the variables accordingly or as you work through this section.

The predefined variables serve as a minimum working example to run the database, backend, frontend and pipelines in the project. Do not use spaces in the variables, specially paths.

The config folder in the root directory serves as a configuration applicable to all components (backend, frontend, pipelines) when deployed using `docker-compose.yml`. For each component, update these variables as needed depending on your runtime environment. 

The config folder is not part of the build image. They will be [ignored](./.dockerignore) during a docker build from the parent folder (e.g. using docker compose). Instead, they are provided at runtime and volume mounts.

```yaml
    volumes:
      - ./config/backend:/app/config:ro
      - ./config/.env:/app/.env:ro
```
```shell
$ docker compose -e SPRING_PROFILES_ACTIVE=dev up   
```

When running the components individually, refer to specific instructions for [backend](./hallo-app-backend/README.md#configure-usernames-passwords-and-other-sensitive-information), [frontend](./hallo-app-frontend/README.md#npm-start), [database](./database/README.md) and [data-pipelines](./data-pipelines/README.md)

> [!Important]
> The variables set in the `hallo-backend/config/` files are automatically loaded when building/loading by Spring Tools Suite and Maven `mvn` package manager.   

### Database

Create a postgres database on the local host as described by the [database installation](./database/README.md#installation) section.
After configuring, provide the database host, port, name, username and password in the .env file.

The database is the only component that is not dockerized. When the database is running locally and other components run from their docker containers, the database hostname must be **"host.docker.internal"**.
 
### Data pipelines

[Data pipelines](./data-pipelines/) for each source are defined in this component. The pipelines, schemas and schedules vary depending on the source.

Environment variables for each data source/provider are configured in `config/<provider name>-pipeline` in `.yaml` files.

### Backend

The [backend](./hallo-app-backend/) is a standalone Spring Boot application with built-in support for API access (Data API). If using docker-compose, the image will be built according to it's dedicated Dockerfile (using maven) and the java jar file will be executed.

The runtime environment variables are specified in `config/.env` and `config/backend/application-*.properties` used by Java. 

### Frontend

The [frontend](./hallo-app-frontend/) is an self-contained React application. If using docker-compose, the image will be built according to it's dedicated Dockerfile (using `npm install`, `npm run build`) and the front end will be served using nginx within the container. 

The docker container has [configures](./nginx/) nginx to use SSL (https); docker-compose renews the certificate. If a new certificate is needed, specify your email in the environment variable `${CERTBOT_EMAIL}` within `config/.env` and, in docker-compose.yml, swap the commented lines in the ssl service command.

```yaml
ssl:
    ...
    command:
        - renew
        #- certonly
        #- --webroot
        #- -w
        #- /var/www/certbot/
        #- --email=${CERTBOT_EMAIL}
        #- --agree-tos
        #- --no-eff-email
        #- -d
        #- portal.orca.research.sfu.ca
```

### Docker

1. Configure your credentials and secrets according to the instructions in its [section](#configure-your-credentials-and-secrets)
> Notice the unquoted path variables

2. Build and start containers with using the explicit .env file
```shell
docker compose --env-file config/.env up --build
```

The front end will be accessible via localhost:80 (ensure port 80 is unoccupied).
> [!NOTE]
> The `.env` from the root directory is automatically shared when building the image and starting a container. To use a specific file you can alternatively prepend the docker compose command with `COMPOSE_ENV_FILES` location.
>
> e.g. `$ COMPOSE_ENV_FILES=config/.env docker compose up `

> [!Important]
> There are multiple ways to provide environment variables. Variety of command line arguments and switches, `COMPOSE_ENV_FILES`, in bulk or explicitly `SPRING_PROFILES_ACTIVE=dev`. 
>
> [!TIP]
> We recommend to use one procedure: the mounted `.env` file and `config` folder (and at most one switch `SPRING_PROFILES_ACTIVE=dev`). 
>
> [!CAUTION]
>This prevents unexpected behaviour due to the rules of precedence. If the switch is already in the .env file, then it is not needed in the command line.