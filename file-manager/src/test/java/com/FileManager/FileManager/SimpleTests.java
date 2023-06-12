package com.FileManager.FileManager;

import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.services.DirectoryService;
import com.FileManager.FileManager.services.EFileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class SimpleTests {

    @Autowired
    public void setDirectoryService(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    private DirectoryService directoryService;
    private EFileService fileService;
    //Смешной первый тест
    //Решил ради обучения написать своей первый смешной тест -> вылился в тонну багов, которые приходилось напильником чистить
    //JUnit сила

    @Test
    void testDirectoryCreation() {
        directoryService.handleDirectoryCreating("JunitTest",0L);
        DirectoryDTO expected = directoryService.findDirectoryByDirName("JunitTest");
        DirectoryDTO actual = directoryService.findLastAddedDirectory();
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        System.out.println(directoryService.deleteDirFromServer(actual.getId()));
        DirectoryDTO actualAfterDelete = directoryService.findDirectoryById(actual.getId());
        assertNull(actualAfterDelete);

    }

}
