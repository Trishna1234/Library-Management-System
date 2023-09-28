package com.trishna.library;

import com.trishna.library.models.Admin;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LibraryManagementSystemProjectApplication implements CommandLineRunner {

	@Autowired
	AdminService adminService;
	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
//		Admin admin = Admin.builder()
//				.name("admin1")
//				.email("admin1")
//				.securedUser(
//						SecuredUser.builder()
//								.username("admin1")
//								.password("admin1")
//								.build()
//				)
//				.build();
//
//		adminService.create(admin);

	}

}
