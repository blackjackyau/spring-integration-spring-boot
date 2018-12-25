package com.blax.spring.integration.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class SftpConfig {

    @Value("${sftp.host}")
    private String sftpHost;

    @Value("${sftp.port:22}")
    private int sftpPort;

    @Value("${sftp.user}")
    private String sftpUser;

    @Value("${sftp.privateKey:#{null}}")
    private String sftpPrivateKey;

    @Value("${sftp.privateKeyPassphrase:}")
    private String sftpPrivateKeyPassphrase;

    @Value("${sftp.password:#{null}}")
    private String sftpPasword;

    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(sftpHost);
        factory.setPort(sftpPort);
        factory.setUser(sftpUser);
        if (sftpPrivateKey != null) {
            ByteArrayResource resource = new ByteArrayResource(sftpPrivateKey.getBytes(Charset.forName("ASCII")));
            factory.setPrivateKey(resource);
            factory.setPrivateKeyPassphrase(sftpPrivateKeyPassphrase);
        } else {
            factory.setPassword(sftpPasword);
        }
        factory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<>(factory);
    }

    //https://stackoverflow.com/questions/43246990/discussion-about-spring-integration-sftp
    //https://github.com/spring-projects/spring-integration/blob/master/spring-integration-sftp/src/test/java/org/springframework/integration/sftp/outbound/SftpServerOutboundTests-context.xml
    @Bean
    @ServiceActivator(inputChannel = "sftpListChannel")
    public MessageHandler listChannel() {
        SftpOutboundGateway sftpOutboundGateway = new SftpOutboundGateway(sftpSessionFactory(), "ls", "payload");
        return sftpOutboundGateway;
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpMGetChannel")
    public MessageHandler mgetChannel() {
        SftpOutboundGateway sftpOutboundGateway =
                new SftpOutboundGateway(sftpSessionFactory(), "mget", "payload.remote()");
        sftpOutboundGateway.setLocalDirectoryExpressionString("payload.local()");
        return sftpOutboundGateway;
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpRMChannel")
    public MessageHandler rmChannel() {
        SftpOutboundGateway sftpOutboundGateway = new SftpOutboundGateway(sftpSessionFactory(), "rm", "payload");
        return sftpOutboundGateway;
    }

    @MessagingGateway(name = "sftpGateway")
    public interface SftpGateway {
        @Gateway(requestChannel = "sftpListChannel")
        List<SftpFileInfo> listFiles(String path);

        @Gateway(requestChannel = "sftpMGetChannel")
        List<File> downloadFiles(DirectoryEntry directoryEntry);

        @Gateway(requestChannel = "sftpRMChannel")
        boolean rmFiles(String path);
    }

    @Data
    @Accessors(fluent = true)
    public static class DirectoryEntry {
        private String local;
        private String remote;
    }

}
