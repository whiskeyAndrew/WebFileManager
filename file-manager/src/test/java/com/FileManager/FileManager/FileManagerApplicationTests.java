package com.FileManager.FileManager;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class FileManagerApplicationTests {

	@BeforeAll
	@Test
	static void beforeAllTest(){
		log.error("Before all error");
	}


	@Test
	@DisplayName("Single test successful")
	void testSingleSuccessTest(){
		log.info("Sussess");
	}

	@Test
	@Disabled("NOT IMPLEMENTED")
	void testShowDisabled(){

	}

	@Test
	void contextLoads() {
	}

}
