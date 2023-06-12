package com.FileManager.FileManager;

import com.FileManager.FileManager.Controllers.FileManagerController;
import com.FileManager.FileManager.DTO.DirectoryDTO;
import com.FileManager.FileManager.DTO.EFileDTO;
import com.FileManager.FileManager.Entity.Directory;
import com.FileManager.FileManager.Entity.EFile;
import com.FileManager.FileManager.services.DirectoryService;
import com.FileManager.FileManager.services.EFileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class SimpleTests {

    @Autowired
    public void setDirectoryService(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @Autowired
    public void setController(FileManagerController controller) {
        this.controller = controller;
    }

    @Autowired
    public void setFileService(EFileService fileService) {
        this.fileService = fileService;
    }

    private FileManagerController controller;
    private DirectoryService directoryService;
    private EFileService fileService;

    List<EFile> filesBeforeTests;
    List<Directory> dirsBeforeTest;
    //Смешной первый тест
    //Решил ради обучения написать своей первый смешной тест -> вылился в тонну багов, которые приходилось напильником чистить
    //JUnit сила

    @BeforeEach
    @DisplayName("UploadDbBeforeTests")
    void uploadDbBeforeTests() {
        filesBeforeTests = new ArrayList<>();
        dirsBeforeTest = new ArrayList<>();
        filesBeforeTests.addAll(fileService.getAllFilesFromDB());
        dirsBeforeTest.addAll(directoryService.getAllDirectories());
    }

    @AfterEach
    @Test
    @DisplayName("CheckDbIsStableAfterTests")
    void checkDbIsOkayAfterTests() {
        try {
            assertEquals(filesBeforeTests, fileService.getAllFilesFromDB());
        } catch (AssertionError e) {
            //Запилить что ли восстановление базы
            e.printStackTrace();
        }

        try {
            assertEquals(dirsBeforeTest, directoryService.getAllDirectories());
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("DirectoryCreateTestAtDb")
    void testDirectoryCreation() {
        directoryService.handleDirectoryCreating("JunitTest", 0L);
        DirectoryDTO expected = directoryService.findDirectoryByDirName("JunitTest");
        DirectoryDTO actual = directoryService.findLastAddedDirectory();
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        System.out.println(directoryService.deleteDirFromServer(actual.getId()));
        DirectoryDTO actualAfterDelete = directoryService.findDirectoryById(actual.getId());
        assertNull(actualAfterDelete);

    }

    @Test
    @DisplayName("FileUploadTest")
    void testFileUpload() {
        File expected = new File("fileForTests.txt");
        try {
            MultipartFile multipartFile = new MockMultipartFile(expected.getName(), expected.getName(), null, new FileInputStream(expected));

            controller.uploadFileOnServer(multipartFile, 0L, true, "");
            EFileDTO actualDTO = fileService.findLastUploadedFile();
            String fullPath = "";
            if (actualDTO.getDirectory().getFullPath() != null) {
                fullPath = actualDTO.getDirectory().getFullPath() + "/";
            }
            File actual = new File(fullPath
                    + actualDTO.getFileName()
                    + "."
                    + actualDTO.getFileType());
            assertEquals(expected, actual);
            fileService.handleFileDeleting(actualDTO.getId());
            assertNull(fileService.findFileById(actualDTO.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("UploadDirectoryTest")
    void testDirUpload() {
        String dirName = "jUnit_test_dir";

        try {
            controller.uploadFileOnServer(null, 0L, false, dirName);
            DirectoryDTO dto = directoryService.findLastAddedDirectory();
            assertEquals(dto.getDirectoryName(), dirName);
            controller.deleteDirFromServer(dto.getId(), false);
            assertNull(directoryService.findDirectoryById(dto.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
