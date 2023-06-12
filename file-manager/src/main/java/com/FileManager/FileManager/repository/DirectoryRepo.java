package com.FileManager.FileManager.repository;

import com.FileManager.FileManager.Entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoryRepo extends JpaRepository<Directory, Long> {
    List<Directory> findDirectoriesByParentDirectoryId(Long id);
    Directory findDirectoryById(Long id);
    Directory findDirectoryByParentDirectoryId(Long id);
    Directory findDirectoryByParentDirectoryIdAndDirectoryName(Long id,  String dirName);
    Directory findFirstByOrderByIdDesc();
    Directory findDirectoryByDirectoryName(String dirName);
    List<Directory> getAllByIdGreaterThan(Long greaterThan);
}
