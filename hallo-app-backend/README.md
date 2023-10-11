# HALLO data portal backend

The goal of this project is to create a platform for HALLO - Humans and Algorithms Listening to Orcas. to support researchers, citizen scientist, the public and protect marine wildlife in the Salish Sea. 

At this stage of the project, our focus is on collecting a variety of data streams and make them accessible to researchers and citizen scientists. 


## Deployment

SFU cloud provides storage and computational power to host databases, webservers, and files.  A Virtual Machine (VP), `hallo-app`` has been provisioned. This VM has a public IP, which can initiate traffic. 
To access to this VM, take the following steps:

    ssh <host-name>
    
    ssh <vm-name>

    ps aux; kill -9 [process of java -jar target/hallo]

    cd hallo-data-portal/

    mvn clean install -DskipTests

    nohup java -jar target/hallo-0.0.1-SNAPSHOT.jar  >/dev/null &


## Environment Setup

  1. For database setup, do the following:

    1.0 Install postgresql 

    1.1. $ psql postgres -U $USER
    1.2. # \du
    
    1.3. # \conninfo
    
    1.4. # create user username;
    
    1.5. # alter user username with superuser;
    
    1.6. # alter user username createdb;
    
    1.7 # \password username
    USE_YOUR_OWN_PASSWORD
    1.8 # \q
    
    
    1.8. $ createdb hallodb -U username
    1.8. $ PGPASSWORD=USE_YOUR_OWN_PASSWORD psql -U username -d hallodb -a -f init.sql
    If the database is remote, use the same command with host
    ```
    $ psql -h host -U username -d myDataBase -a -f myInsertFile
    ```

    1.8. PGPASSWORD=USE_YOUR_OWN_PASSWORD psql -U username hallodb
    To connect to postgres database
    
    1.9. Allow local/remove connection (peer authentication error)
    
    
    For Red Hat set up see: https://www.postgresql.org/download/linux/redhat/
    
    For MacOS `brew install postgresql@13` and `brew services run postgresql@13`)


2. System setup

  2.1. Install sshpass
    
  2.2 Install jq command

  2.3.
    Install python 3 on Red Hat Enterprise
