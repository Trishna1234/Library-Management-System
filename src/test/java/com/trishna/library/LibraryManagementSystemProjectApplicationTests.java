package com.trishna.library;

import com.trishna.library.controllers.AdminController;
import com.trishna.library.controllers.BookController;
import com.trishna.library.controllers.StudentController;
import com.trishna.library.controllers.TransactionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LibraryManagementSystemProjectApplicationTests {

	@Autowired
	AdminController adminController;
	@Autowired
	BookController bookController;
	@Autowired
	StudentController studentController;
	@Autowired
	TransactionController transactionController;

	@Test
	void contextLoads() {
		assertThat(adminController).isNotNull();
		assertThat(studentController).isNotNull();
		assertThat(bookController).isNotNull();
		assertThat(transactionController).isNotNull();
	}

}
