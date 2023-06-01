package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.Entity.Directory;
import org.springframework.stereotype.Service;

@Service
public class DirectoryService {

    private DirectoryDTO convertToDTO(Directory directory){
        DirectoryDTO directoryDTO = new DirectoryDTO();
        directoryDTO.setDirectoryName(directory.getDirectoryName());
        directoryDTO.setParentDirectory(directory.getParentDirectory());
        directoryDTO.setFullPath(directory.getFullPath());
        directoryDTO.setId(directory.getId());
        return directoryDTO;
    }
}
