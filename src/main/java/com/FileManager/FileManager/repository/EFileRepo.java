package com.FileManager.FileManager.repository;

import com.FileManager.FileManager.Entity.EFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EFileRepo extends JpaRepository<EFile,Long> {
    EFile findFileById(Long id);
    List<EFile> findFileByDirectoryId(Long id);
}
