package com.trishna.library.unitTest;

import com.trishna.library.controllers.AdminController;
import com.trishna.library.dtos.GetAdminResponse;
import com.trishna.library.models.Admin;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTo() {
        // Create a sample instance of YourClass
        Admin adminInstance = new Admin();
        adminInstance.setId(1);
        adminInstance.setName("John");
        adminInstance.setEmail("john@example.com");

        // Create a mock of the secured user (you might need a mocking framework like Mockito)
        SecuredUser mockUser = new SecuredUser();
        mockUser.setUsername("john_user");

        // Set the secured user for yourInstance
        adminInstance.setSecuredUser(mockUser);

        // Call the to() method
        GetAdminResponse adminResponse = adminInstance.to();
        assertEquals(1, adminResponse.getId());
        assertEquals("John", adminResponse.getName());
        assertEquals("john@example.com", adminResponse.getEmail());
        assertEquals("john_user", adminResponse.getUsername());
    }


}
