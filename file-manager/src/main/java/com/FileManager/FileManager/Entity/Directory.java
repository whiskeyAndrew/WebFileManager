package com.FileManager.FileManager.Entity;

import com.FileManager.FileManager.DTO.Interfaces.IDirectory;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Directory implements IDirectory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String directoryName;
    private String fullPath;

    @ManyToOne
    @JoinColumn(name = "parent_directory")
    private Directory parentDirectory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        return Objects.equals(id, directory.id) && Objects.equals(directoryName, directory.directoryName) && Objects.equals(fullPath, directory.fullPath) && Objects.equals(parentDirectory, directory.parentDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, directoryName, fullPath, parentDirectory);
    }
}
