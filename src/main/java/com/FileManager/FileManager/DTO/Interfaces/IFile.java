package com.FileManager.FileManager.DTO.Interfaces;

import com.FileManager.FileManager.DTO.Interfaces.IEntity;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.Owner;

public interface IFile extends IEntity {

    void setId(Long id);

    String getFileName();

    void setFileName(String fileName);

    String getFileType();

    void setFileType(String fileType);

    Long getFileSize();

    void setFileSize(Long fileSize);

    String getFilePath();

    void setFilePath(String filePath);

    Directory getDirectory();

    void setDirectory(Directory directory);

    Owner getOwner();

    void setOwner(Owner owner);
}
