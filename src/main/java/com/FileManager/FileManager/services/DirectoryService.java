package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.repository.DirectoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {
    private final DirectoryRepo directoryRepo;

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
        directory.setFullPath(directory.getFullPath());
        return directory;
    }
}
