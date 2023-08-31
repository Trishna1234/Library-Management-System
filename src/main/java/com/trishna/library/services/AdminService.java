package com.trishna.library.services;

import com.trishna.library.dtos.GetAdminResponse;
import com.trishna.library.dtos.UpdateRequest;
import com.trishna.library.models.Admin;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.models.Student;
import com.trishna.library.repositories.AdminRepository;
import com.trishna.library.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserService userService;

    public void create(Admin admin) {
        SecuredUser securedUser = admin.getSecuredUser();
        securedUser = userService.save(securedUser, Constants.ADMIN_USER);

        admin.setSecuredUser(securedUser);
        adminRepository.save(admin);
    }
    public Admin find(Integer adminId){
        return adminRepository.findById(adminId).orElse(null);
    }

    public GetAdminResponse view(Integer adminId){
        Admin admin = adminRepository.findById(adminId).orElse(null);
        return admin.to();
    }

    public void updateDetails(int adminId, UpdateRequest request) throws Exception {
        Admin retrievedAdmin = adminRepository.findById(adminId).orElse(null);
        String key = request.getUpdateKey();
        String value = request.getUpdateValue();
        if(retrievedAdmin != null){
            switch (key){
                case "name" : {
                    retrievedAdmin.setName(value);
                    break;
                }
                case "email" : {
                    retrievedAdmin.setEmail(value);
                    break;
                }
                default:
                    throw new Exception("Invalid update key");
            }
            adminRepository.save(retrievedAdmin);
        }
        else throw new Exception("Student Not found");
    }
}
