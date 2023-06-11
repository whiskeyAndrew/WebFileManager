package com.FileManager.FileManager.Entity;

import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Directory implements IDirectory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String directoryName;
    private String fullPath;

    @ManyToOne
    @JoinColumn(name = "parent_directory")
    private Directory parentDirectory;

}
