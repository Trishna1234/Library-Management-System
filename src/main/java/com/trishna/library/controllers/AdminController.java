package com.trishna.library.controllers;

import com.trishna.library.dtos.CreateAdminRequest;
import com.trishna.library.dtos.GetAdminResponse;
import com.trishna.library.dtos.UpdateRequest;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

//    Create Admin
    @PostMapping("/admin")
    public void create(@RequestBody @Valid CreateAdminRequest createAdminRequest){
        adminService.create(createAdminRequest.to());
    }

//    View Admin Details
    @GetMapping("/admin/view")
    public GetAdminResponse find(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int adminId = securedUser.getAdmin().getId();

        return adminService.view(adminId);
    }

//    Update Admin Details
    @PatchMapping("/admin/update")
    public void updateDetails(@RequestBody UpdateRequest updateRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int adminId = securedUser.getAdmin().getId();

        adminService.updateDetails(adminId, updateRequest);
    }
}
