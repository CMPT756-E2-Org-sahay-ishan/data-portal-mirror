# HALLO database


## Installation

Install postgresql 
- For Red Hat set up see: https://www.postgresql.org/download/linux/redhat/

- For MacOS `brew install postgresql@13` and `brew services run postgresql@13`)

To setup a database setup, follow the instructions below.

1. Login to postgres and obtain a psql prompt (`#`)
```psql
$ psql postgres -U $USER
# \du
# \conninfo
```

2. Create a user and update the permissions and password. The last command will exit the psql prompt.
```
# create user <username>;
# alter user <username> with superuser;
# alter user <username> createdb;
# \password <username>;
USE_YOUR_OWN_PASSWORD
# \q
```

3. Create a dedicated database named `hallodb`
```shell
$ createdb hallodb -U username
```

4. Initialize the database with [`init.sql`](./init.sql)

```$ PGPASSWORD=USE_YOUR_OWN_PASSWORD psql -U <username> -d hallodb -a -f init.sql```

> [!Important]
> If the database is on a remote machine, use the same command with `-h <host_name>`
>
> `$ psql -h <host> -U <username> -d myDatabase -a -f myInsertFile`

5. To connect to the new postgres database
```shell
PGPASSWORD=USE_YOUR_OWN_PASSWORD psql -U <username> hallodb
```

6. Allow local/remove connection (peer authentication error)


## Docker

To permit access by the docker containers running locally by [docker-compose.yml](../docker-compose.yml), update the postgres configuration to allow connections from a local subnet. 
1. Edit the file `vim /opt/homebrew/var/postgresql@13/pg_hba.conf` with any text editor. Change _postgresql@13_ with applicable path. 
2. Add the following line at the end and save.
```
host    all     all     10.5.0.0/16     md5
```
