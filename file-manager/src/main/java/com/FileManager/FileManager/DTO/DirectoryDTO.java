package com.FileManager.FileManager.DTO;

import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import com.FileManager.FileManager.Entity.Directory;
import lombok.Data;

@Data
public class DirectoryDTO implements IDirectory {
    private Long id;
    private String directoryName;
    private Directory parentDirectory;
    private String fullPath;
}

