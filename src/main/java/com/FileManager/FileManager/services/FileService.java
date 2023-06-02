package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.FileDTO;
import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.File;
import com.FileManager.FileManager.repository.FileRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;

    @PostConstruct
    void init() {

    }

    public FileDTO findFileById(Long id){
        return convertToDTO(fileRepo.findFileById(id));
    }
    public List<FileDTO> findFilesByDirectoryId(Long id){
        List<File> files = fileRepo.findFileByDirectoryId(id);
        List<FileDTO> filesDTO = new ArrayList<>();

        for(File f:files){
            filesDTO.add(convertToDTO(f));
        }
        return filesDTO;
    }

    private File convertToSQL(FileDTO fileDTO){
        File file = new File();
        file.setId(fileDTO.getId());
        file.setFileName(fileDTO.getFileName());
        file.setFilePath(fileDTO.getFilePath());
        file.setFileSize(fileDTO.getFileSize());
        file.setFileType(fileDTO.getFileType());
        return file;
    }
    private FileDTO convertToDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFilePath(file.getFilePath());
        fileDTO.setFileSize(file.getFileSize());
        fileDTO.setFileType(file.getFileType());
        fileDTO.setId(file.getId());
        return fileDTO;
    }

}
