package com.blax.spring.integration.sftp;

import org.apache.tomcat.jni.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path="/sftp")
public class SftpController {

    @Autowired
    private SftpConfig.SftpGateway gateway;

    private final String tempFolder = System.getProperty("java.io.tmpdir");

    @RequestMapping("/ls")
    public String index(@RequestParam("path") String path) {
        List<SftpFileInfo> output = gateway.listFiles(path);
        return output.toString();
    }

    @RequestMapping("/mget")
    public String mdownload(@RequestParam("path") String path) {
        UUID uuid = UUID.randomUUID();
        String randomGeneratedTempFolder = String.format("%ssftp-%s", tempFolder, uuid.toString());
        SftpConfig.DirectoryEntry entry = new SftpConfig.DirectoryEntry(randomGeneratedTempFolder, path);
        List<File> files = gateway.downloadFiles(entry);
        return files.toString();
    }

    @RequestMapping("/rm")
    public String rm(@RequestParam("path") String path) {
        boolean result = gateway.rmFiles(path);
        return String.valueOf(result);
    }
}
