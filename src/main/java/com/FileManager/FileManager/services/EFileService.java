package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.EFileDTO;
import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.Entity.EFile;
import com.FileManager.FileManager.repository.EFileRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EFileService {
    private final EFileRepo fileRepo;
    private final DirectoryService directoryService;

    @Value("${rootFolder}")
    private String rootFolder;

    @PostConstruct
    private void init() {
        if (!rootFolder.endsWith("/")) {
            rootFolder = rootFolder + "/";
        }
    }

    public void saveFileToDB(EFileDTO file){
        EFile fileSQL = convertToSQL(file);
        fileRepo.save(fileSQL);
    }
    public EFileDTO findFileById(Long id){
        return convertToDTO(fileRepo.findFileById(id));
    }
    public List<EFileDTO> findFilesByDirectoryId(Long id){
        List<EFile> files = fileRepo.findFileByDirectoryId(id);
        List<EFileDTO> filesDTO = new ArrayList<>();

        for(EFile f:files){
            filesDTO.add(convertToDTO(f));
        }
        return filesDTO;
    }

    private EFile convertToSQL(EFileDTO fileDTO){
        EFile file = new EFile();
        file.setId(fileDTO.getId());
        file.setFileName(fileDTO.getFileName());
        file.setFilePath(fileDTO.getFilePath());
        file.setFileSize(fileDTO.getFileSize());
        file.setFileType(fileDTO.getFileType());
        file.setDirectory(fileDTO.getDirectory());
        return file;
    }
    private EFileDTO convertToDTO(EFile file) {
        EFileDTO fileDTO = new EFileDTO();
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFilePath(file.getFilePath());
        fileDTO.setFileSize(file.getFileSize());
        fileDTO.setFileType(file.getFileType());
        fileDTO.setId(file.getId());
        return fileDTO;
    }

    public void handleFileSaving(MultipartFile file, Long dirId) {
        if (file.isEmpty() || file.getName().equals("")) {
            return;
        }

        try {
            String fileName = file.getOriginalFilename();
            IDirectory currentDirectory = directoryService.findDirectoryById(dirId);
            StringBuilder filePath = new StringBuilder(rootFolder);
            if (currentDirectory.getFullPath() != null) {
                filePath.append(currentDirectory.getFullPath());
            }
            filePath.append(fileName);
            java.io.File incomingFile = new File(filePath.toString());
            OutputStream os = new FileOutputStream(incomingFile);
            os.write(file.getBytes());

            os.flush();
            os.close();

            EFileDTO eFile = new EFileDTO();
            assert fileName != null;
            eFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
            eFile.setFilePath("");
            eFile.setFileSize(incomingFile.length());
            eFile.setDirectory(DirectoryService.convertToSql(directoryService.findDirectoryById(dirId)));
            eFile.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
            saveFileToDB(eFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
