package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.repository.DirectoryRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {
    private final DirectoryRepo directoryRepo;

    @Value("${rootFolder}")
    private String rootFolder;
    @PostConstruct
    private void init() {
        if (!rootFolder.endsWith("/")) {
            rootFolder = rootFolder + "/";
        }
    }

    public void saveDirectoryInDB(DirectoryDTO directory){
        directoryRepo.save(convertToSql(directory));
    }
    public List<DirectoryDTO> findDirectoriesByParentDirectoryId(Long id){
        List<Directory> directories = directoryRepo.findDirectoryByParentDirectoryId(id);
        List<DirectoryDTO> directoryDTOS = new ArrayList<>();
        for(Directory d: directories){
            directoryDTOS.add(convertToDTO(d));
        }
        return directoryDTOS;
    }

    public DirectoryDTO findDirectoryById(Long id){
        Directory directory = directoryRepo.findDirectoryById(id);
        return convertToDTO(directory);
    }

    private DirectoryDTO convertToDTO(Directory directory){
        DirectoryDTO directoryDTO = new DirectoryDTO();
        directoryDTO.setDirectoryName(directory.getDirectoryName());
        directoryDTO.setParentDirectory(directory.getParentDirectory());
        directoryDTO.setFullPath(directory.getFullPath());
        directoryDTO.setId(directory.getId());
        return directoryDTO;
    }

    static public Directory convertToSql(DirectoryDTO directoryDTO){
        Directory directory = new Directory();
        directory.setDirectoryName(directoryDTO.getDirectoryName());
        directory.setParentDirectory(directoryDTO.getParentDirectory());
        directory.setId(directoryDTO.getId());
        directory.setFullPath(directoryDTO.getFullPath());
        return directory;
    }

    public void handleDirectoryCreating(String dirName, Long dirId) {

        Directory parentDirectory = DirectoryService.convertToSql(findDirectoryById(dirId));
        StringBuilder fullPath = new StringBuilder();
        if (parentDirectory.getFullPath() != null) {
            fullPath.append(parentDirectory.getFullPath());
        }
        fullPath.append(dirName);

        File dir = new File(rootFolder + fullPath);
        if (!dir.exists()) {
            boolean isCreated = dir.mkdirs();
        } else {
            return;
        }
        DirectoryDTO directory = new DirectoryDTO();
        directory.setDirectoryName(dirName);
        directory.setParentDirectory(parentDirectory);
        directory.setFullPath(fullPath + "/");

        saveDirectoryInDB(directory);
    }
}
