## Spring integration SFTP

- How to use SftpOutboundGateway with MessagingGateway (pure interface)
- start sftp server from sftp-compose project `docker-compose up`
- start gradle project
- to list file from remote server `http://localhost:8080/v0/sftp/ls?path=outbox/`
- to download all files to local temp `http://localhost:8080/v0/sftp/mget?path=outbox/`
- to download individual file to local temp `http://localhost:8080/v0/sftp/mget?path=outbox/myfile.png`
- to download files that matches pattern to local temp `http://localhost:8080/v0/sftp/mget?path=outbox/*.png`
- to remove all files from sftp server `http://localhost:8080/v0/sftp/rm?path=outbox/*.*`
