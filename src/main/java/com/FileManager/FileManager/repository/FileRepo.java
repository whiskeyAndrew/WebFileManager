package com.FileManager.FileManager.repository;

import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepo extends JpaRepository<File,Long> {
    List<File> findAllByDirectory(Directory directory);
    List<File> findAllByDirectoryIsNull();
    File findFileById(Long id);
}
