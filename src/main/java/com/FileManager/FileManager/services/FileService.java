package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.FileDTO;
import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.Entity.File;
import com.FileManager.FileManager.repository.FileRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;

    @PostConstruct
    void init() {

    }

    public IFile findFileById(Long id) {
        File file = fileRepo.findFileById(id);
        if (file == null) {
            return null;
        }
        return convertToDTO(file);
    }

    private FileDTO convertToDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFilePath(fileDTO.getFilePath());
        fileDTO.setFileSize(fileDTO.getFileSize());
        fileDTO.setFileType(fileDTO.getFileType());
        file.setId(file.getId());
        return fileDTO;
    }

}
