package com.FileManager.FileManager.Controllers;

import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.DTO.EFileDTO;
import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.EFile;
import com.FileManager.FileManager.services.DirectoryService;
import com.FileManager.FileManager.services.EFileService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;

@RequiredArgsConstructor
@Controller
public class FileUploadController {
    private final EFileService fileService;
    private final DirectoryService directoryService;

    @Value("${rootFolder}")
    private String rootFolder;

    @PostConstruct
    private void init() {
        if (!rootFolder.endsWith("/")) {
            rootFolder = rootFolder + "/";
        }
    }

    @PostMapping("/dirs/{id}")
    private RedirectView uploadFileOnServer(@RequestParam(name = "fileToUpload", required = false) MultipartFile file,
                                            @PathVariable(name = "id") Long dirId,
                                            @RequestParam(name = "isItFile") boolean isItFile,
                                            @RequestParam(name = "dirName") String dirName) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/dirs/" + dirId);

        if (isItFile) {
            fileService.handleFileSaving(file, dirId);
        } else {
            directoryService.handleDirectoryCreating(dirName, dirId);
        }
        return redirectView;
    }


}
