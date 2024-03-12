# HALLO Data Portal

The goal of this project is to create a platform for HALLO - Humans and Algorithms Listening to Orcas (and the coastal-science organization) to support researchers, citizen scientist, the public and protect marine wildlife in the Salish Sea. 

At this stage of the project, our focus is on collecting a variety of data streams and make them accessible to researchers and citizen scientists. 

This work was completed in part with resources provided by the Research Computing Group, IT Services at Simon Fraser University, including the use of the Cedar / Cedar Cloud and the advanced support from the Research Computing Group, to carry out the development presented here.

# Table of Contents
1. [Getting Started](#getting-started)
2. [Database](#database)
3. Standalone [Backend](./hallo-app-backend/README.md "standalone backend") application
4. Standalone [Frontend](./hallo-app-frontend/README.md "standalone frontend") application
5. Standalone [data-pipelines](./data-pipelines/README.md "standalone pipelines")
6. [Docker](#docker)

## Getting started
To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.rcg.sfu.ca/hallo/hallo-data-portal.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.rcg.sfu.ca/hallo/hallo-data-portal/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

# Editing this README

When you're ready to make this README your own, just edit this file and use the handy template below (or feel free to structure it however you want - this is just a starting point!). Thank you to [makeareadme.com](https://www.makeareadme.com/) for this template.

## Suggestions for a good README
Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Choose a self-explaining name for your project.

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.

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