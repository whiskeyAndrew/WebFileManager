package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.EFileDTO;
import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.Entity.EFile;
import com.FileManager.FileManager.RabbitMQ.service.RabbitMessageSender;
import com.FileManager.FileManager.repository.EFileRepo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EFileService {
    private final EFileRepo fileRepo;
    private final DirectoryService directoryService;
    @Value("${rootFolder}")
    private String rootFolder;
    private final RabbitMessageSender rabbitSender;

    @PostConstruct
    private void init() {
        if (!rootFolder.endsWith("/")) {
            rootFolder = rootFolder + "/";
        }
    }

    public List<EFile> getAllFilesFromDB(){
        return fileRepo.getAllByIdGreaterThan(-1L);
    }
    public void saveFileToDB(EFileDTO file) {
        EFile fileSQL = convertToSQL(file);
        fileRepo.save(fileSQL);
    }

    public EFileDTO findFileById(Long id) {
        EFile file = fileRepo.findFileById(id);
        if(file==null){
            return null;
        }
        return convertToDTO(file);
    }

    public List<EFileDTO> findFilesByDirectoryId(Long id) {
        List<EFile> files = fileRepo.findFileByDirectoryId(id);
        List<EFileDTO> filesDTO = new ArrayList<>();

        for (EFile f : files) {
            filesDTO.add(convertToDTO(f));
        }
        return filesDTO;
    }

    private EFile convertToSQL(EFileDTO fileDTO) {
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
        fileDTO.setDirectory(file.getDirectory());
        return fileDTO;
    }

    public boolean handleFileDeleting(Long fileId) {
        EFile eFile = fileRepo.findFileById(fileId);
        if (eFile == null) {
            return false;
        }
        StringBuilder filePath = new StringBuilder();
        String fileLocalPath = "";
        if (eFile.getDirectory().getId() != 0) {
            fileLocalPath = eFile.getDirectory().getFullPath();
        }
        filePath.append(rootFolder)
                .append(fileLocalPath)
                .append(eFile.getFileName())
                .append(".")
                .append(eFile.getFileType());

        //Нужно как-то обдумать, чтобы были бэкапы
        fileRepo.delete(eFile);

        File file = new File(filePath.toString());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientAddr = request.getRemoteAddr();
        rabbitSender.sendMessage("Удален файл: "
                + eFile.getFilePath()
                + "/"
                + eFile.getFileName()
                + "."
                + eFile.getFileType() + ", удалил: " + clientAddr);

        return file.delete();
    }

    public boolean handleFileSaving(MultipartFile file, Long dirId) {
        if (file.isEmpty() || file.getName().equals("")) {
            return false;
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

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String clientAddr = request.getRemoteAddr();

            rabbitSender.sendMessage("Добавлен файл: "
                    + eFile.getFilePath()
                    + "/"
                    + eFile.getFileName()
                    + "."
                    + eFile.getFileType()
                    + ", добавил: "
                    + clientAddr);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public EFileDTO findLastUploadedFile(){
        return convertToDTO(fileRepo.findFirstByOrderByIdDesc());
    }


}
