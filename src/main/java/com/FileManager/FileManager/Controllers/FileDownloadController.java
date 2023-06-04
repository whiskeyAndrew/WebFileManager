package com.FileManager.FileManager.Controllers;

import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.services.DirectoryService;
import com.FileManager.FileManager.services.EFileService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class FileDownloadController {
    private final EFileService fileService;
    private final DirectoryService directoryService;
    @Value("${rootFolder}")
    private String rootFolder;

    @PostConstruct
    void init() {
        if (!rootFolder.endsWith("/")) {
            rootFolder = rootFolder + "/";
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamSource> downloadFile(@PathVariable("id") Long fileId) {
        IFile file = fileService.findFileById(fileId);
        String fullPath = "";
        if (file.getDirectory() != null) {
            IDirectory parentDirectory = directoryService.findDirectoryById(file.getDirectory().getId());
            if (parentDirectory.getFullPath() != null) {
                fullPath = parentDirectory.getFullPath();
            }
        }

        StringBuilder filePath = new StringBuilder();
        filePath.append(rootFolder).append(fullPath).append(file.getFileName()).append(".").append(file.getFileType());
        try {


            InputStream inputStream = new FileInputStream(filePath.toString());
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file.getFileName() + "." + file.getFileType());

            return ResponseEntity.ok().headers(headers).body(inputStreamResource);
        } catch (IOException ignored) {
        }
        return ResponseEntity.noContent().build();
    }
}
