package com.trishna.library.unitTest;

import com.trishna.library.dtos.GetAdminResponse;
import com.trishna.library.models.Admin;
import com.trishna.library.models.SecuredUser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class AdminControllerTest {

    @Test
    public void testTo() {
        // Create a sample instance of YourClass
        Admin yourInstance = new Admin();
        yourInstance.setId(1);
        yourInstance.setName("John");
        yourInstance.setEmail("john@example.com");

        // Create a mock of the secured user (you might need a mocking framework like Mockito)
        SecuredUser mockUser = new SecuredUser();
        mockUser.setUsername("john_user");

        // Set the secured user for yourInstance
        yourInstance.setSecuredUser(mockUser);

        // Call the to() method
        GetAdminResponse adminResponse = yourInstance.to();
        assertEquals(1, adminResponse.getId());
        assertEquals("John", adminResponse.getName());
        assertEquals("john@example.com", adminResponse.getEmail());
        assertEquals("john_user", adminResponse.getUsername());
    }
}
