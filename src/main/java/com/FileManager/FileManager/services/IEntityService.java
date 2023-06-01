package com.FileManager.FileManager.services;

import com.FileManager.FileManager.DTO.Interfaces.IFile;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.repository.DirectoryRepo;
import com.FileManager.FileManager.repository.FileRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IEntityService {
    private final FileRepo fileRepo;
    private final DirectoryRepo directoryRepo;

    @PostConstruct
    void init(){

    }

    /*public IFile findFileById(Long id){
        IFile file = fileRepo.findById(1L).get();
        return file;
    }

    public  List<Directory> findDirectoryAtRoot(){
        List<Directory> directories =  directoryRepo.findDirectoriesByParentDirectoryIsNull();
        return directories;
    }*/
}
