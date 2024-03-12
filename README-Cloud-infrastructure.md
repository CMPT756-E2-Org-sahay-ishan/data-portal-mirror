# Cloud infrastructure

SFU cloud provides storage and computational power to host databases, web servers, and files.  A Virtual Machine (VM), `<vm-name>` has been provisioned. This VM has a public IP, which can initiate traffic. 

## VM configuration

The data portal application are deployed on a dedicated VM instance with 1.5 TB volume attached on Cedar Cloud.

The VM is configured with
- Red Hat Enterprise
- git
- docker
- python
- postgres
