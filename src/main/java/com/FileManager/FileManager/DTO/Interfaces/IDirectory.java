package com.FileManager.FileManager.DTO.Interfaces;

import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.Owner;

public interface IDirectory extends IEntity{
    Long getId();

    void setId(Long id);

    String getDirectoryName();

    void setDirectoryName(String directoryName);

    Directory getParentDirectory();

    void setParentDirectory(Directory parentDirectory);

    String getFullPath();

    void setFullPath(String fullPath);

}
