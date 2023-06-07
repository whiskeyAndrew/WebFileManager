package com.FileManager.FileManager.DTO;

import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.Owner;
import lombok.Data;

@Data
public class EFileDTO implements IFile {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private Directory directory;
    private Owner owner;
}
