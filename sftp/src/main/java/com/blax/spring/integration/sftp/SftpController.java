package com.blax.spring.integration.sftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController()
@RequestMapping(path="/sftp")
public class SftpController {

    @Autowired
    private SftpConfig.SftpGateway gateway;

    @RequestMapping("/ls")
    public String index(@RequestParam("path") String path) {
        List<SftpFileInfo> output = gateway.listFiles(path);
        return output.toString();
    }

    @RequestMapping("/mget")
    public String mdownload() {
        List<File> files = gateway.downloadFiles(null);
        return files.toString();
    }

    @RequestMapping("/rm")
    public String rm() {
        boolean result = gateway.rmFiles("outbox/*.*");
        return String.valueOf(result);
    }
}
