package com.FileManager.FileManager.Controllers;

import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.RabbitMQ.service.RabbitMessageSender;
import com.FileManager.FileManager.services.DirectoryService;
import com.FileManager.FileManager.services.EFileService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class FileManagerController {

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

    @GetMapping("/dirs/{id}")
    private String getDirectoryById(@PathVariable("id") Long dirId, Model model) {
        IDirectory currentDirectory = directoryService.findDirectoryById(dirId);
        long parentDirectoryId;

        if (currentDirectory.getParentDirectory() == null) {
            parentDirectoryId = -1;
        } else {
            parentDirectoryId = currentDirectory.getParentDirectory().getId();
        }

        model.addAttribute("parentDirId", parentDirectoryId);
        model.addAttribute("files", fileService.findFilesByDirectoryId(dirId));
        model.addAttribute("dirs", directoryService.findDirectoriesByParentDirectoryId(dirId));
        model.addAttribute("currentDirId", dirId);

        return "filesPage";
    }

    @PostMapping("/dirs/{id}")
    public RedirectView uploadFileOnServer(@RequestParam(name = "fileToUpload", required = false) MultipartFile file,
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/file/{id}")
    public ResponseEntity<RedirectView> deleteFileFromServer(@PathVariable(name = "id") Long fileId) {
        boolean result = fileService.handleFileDeleting(fileId);
        if (result) {
            return ResponseEntity.ok(new RedirectView());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/dir/{id}")
    public ResponseEntity<RedirectView> deleteDirFromServer(@PathVariable(name = "id") Long dirId, @RequestParam(name = "isDeepDelete") Boolean isDeepDelete) {
        int result;
        if (isDeepDelete) {
            result = directoryService.deepDeleteFromServer(dirId);
        } else {
            result = directoryService.deleteDirFromServer(dirId);
        }
        switch (result) {
            case 0 -> {
                return ResponseEntity.ok(new RedirectView());
            }
            case 1 -> {
                return ResponseEntity.notFound().build();
            }
            case 2 -> {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }
            case 3 -> {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }
}
