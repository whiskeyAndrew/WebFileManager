package com.FileManager.FileManager.Controllers;

import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import com.FileManager.FileManager.services.DirectoryService;
import com.FileManager.FileManager.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class FileManagerController {

    private final FileService fileService;
    private final DirectoryService directoryService;

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
        return "filesPage";
    }
}
