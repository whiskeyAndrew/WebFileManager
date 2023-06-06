package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.EFile;
import com.FileManager.FileManager.repository.DirectoryRepo;
import com.FileManager.FileManager.repository.EFileRepo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectoryService {
    private final DirectoryRepo directoryRepo;
    private final EFileRepo fileRepo;

    @Value("${rootFolder}")
    private String rootFolder;

    @PostConstruct
    private void init() {
        if (!rootFolder.endsWith("/")) {
            rootFolder = rootFolder + "/";
        }
    }

    public void saveDirectoryInDB(DirectoryDTO directory) {
        directoryRepo.save(convertToSql(directory));
    }

    public List<DirectoryDTO> findDirectoriesByParentDirectoryId(Long id) {
        List<Directory> directories = directoryRepo.findDirectoriesByParentDirectoryId(id);
        List<DirectoryDTO> directoryDTOS = new ArrayList<>();
        for (Directory d : directories) {
            directoryDTOS.add(convertToDTO(d));
        }
        return directoryDTOS;
    }

    public DirectoryDTO findDirectoryById(Long id) {
        Directory directory = directoryRepo.findDirectoryById(id);
        return convertToDTO(directory);
    }

    private DirectoryDTO convertToDTO(Directory directory) {
        DirectoryDTO directoryDTO = new DirectoryDTO();
        directoryDTO.setDirectoryName(directory.getDirectoryName());
        directoryDTO.setParentDirectory(directory.getParentDirectory());
        directoryDTO.setFullPath(directory.getFullPath());
        directoryDTO.setId(directory.getId());
        return directoryDTO;
    }

    static public Directory convertToSql(DirectoryDTO directoryDTO) {
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


    public int deleteDirFromServer(Long dirId) {
        Directory directory = directoryRepo.findDirectoryById(dirId);
        if (directory == null) {
            return 1;
        }
        if (directory.getId() == 0) {
            return 1;
        }
        StringBuilder dirPath = new StringBuilder().append(rootFolder).append(directory.getFullPath());
        //full path хранит путь с / в конце, он нам не нужен
        dirPath.deleteCharAt(dirPath.length() - 1);

        File file = new File(dirPath.toString());
        File[] files = file.listFiles();

        if (files != null && files.length > 0) {
            return 2;
        }

        if (file.delete())
            directoryRepo.delete(directory);
        else {
            return 3;
        }
        return 0;
    }

    public int deepDeleteFromServer(Long dirId) {
        Directory directory = directoryRepo.findDirectoryById(dirId);
        if (directory == null) {
            return 1;
        }
        if (directory.getId() == 0) {
            return 1;
        }

        deleteFiles(directory);
        return 0;
    }

    private void deleteFiles(Directory directory) {
        StringBuilder dirPath = new StringBuilder().append(rootFolder).append(directory.getFullPath());
        //full path хранит путь с / в конце, он нам не нужен
        dirPath.deleteCharAt(dirPath.length() - 1);
        File folder = new File(dirPath.toString());
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        Directory dirInside = directoryRepo.findDirectoryByParentDirectoryIdAndDirectoryName(directory.getId(), file.getName());
                        deleteFiles(dirInside);

                    } else {
                        String fileName = file.getName();

                        EFile eFile = fileRepo.findEFileByFileNameAndFileTypeAndDirectory(
                                file.getName().substring(0, fileName.lastIndexOf('.')),
                                file.getName().substring(fileName.lastIndexOf('.') + 1, fileName.length()),
                                directory);
                        if (eFile != null) {
                            fileRepo.delete(eFile);
                            boolean result = file.delete();
                        }

                    }
                }
            }
        }
        directoryRepo.delete(directory);
        boolean folderDeleteResult = folder.delete();

    }
}
