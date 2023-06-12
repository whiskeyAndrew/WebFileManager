package com.FileManager.FileManager.Entity;

import com.FileManager.FileManager.DTO.Interfaces.IFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class EFile implements IFile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "directory_id")
    private Directory directory;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EFile eFile = (EFile) o;
        return Objects.equals(id, eFile.id) && Objects.equals(fileName, eFile.fileName) && Objects.equals(fileType, eFile.fileType) && Objects.equals(fileSize, eFile.fileSize) && Objects.equals(filePath, eFile.filePath) && Objects.equals(directory, eFile.directory) && Objects.equals(owner, eFile.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, fileType, fileSize, filePath, directory, owner);
    }

}
