package com.trishna.library.controllers;


import com.trishna.library.dtos.CreateStudentRequest;
import com.trishna.library.dtos.GetBookResponse;
import com.trishna.library.dtos.GetStudentResponse;
import com.trishna.library.dtos.UpdateRequest;
import com.trishna.library.exceptions.user.UserAlreadyExistsException;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.models.Student;
import com.trishna.library.services.StudentService;
import com.trishna.library.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

//    Creation of Student
    @PostMapping("/student/create")
    public void createStudent(@RequestBody @Valid CreateStudentRequest createStudentRequest){
        Student student = studentService.findByEmail(createStudentRequest.getEmail());
        if(student != null)
            throw new UserAlreadyExistsException("Student Already Exists");
        studentService.create(createStudentRequest.to());
    }

    // Only for Admin so that they can see any student's details
    @GetMapping("/student-by-id/{id}")
    public GetStudentResponse findStudentById(@PathVariable("id") Integer studentId) throws Exception {
        if(isAdmin()){
            return studentService.find(studentId);
        }
        throw new Exception("User is not authorized");
    }

//    Admin view Student's issued books by ID
    @GetMapping("/student-by-id/book/{id}")
    public List<GetBookResponse> findStudentBooksById(@PathVariable("id") Integer studentId) throws Exception{
        if(isAdmin()){
            return studentService.getMyBooks(studentId);
        }
        throw new Exception("User is not authorized");
    }

//    Admin update Student details by ID
    @PatchMapping("/student-by-id/update/{id}")
    public void updateStudentDetails(@PathVariable("id") Integer studentId, @RequestBody UpdateRequest updateRequest) throws Exception {
        boolean isCalledByAdmin = isAdmin();
        if(isCalledByAdmin){
            studentService.updateDetails(studentId, updateRequest);
        }
    }

//    Admin Delete Student by ID
    @DeleteMapping("/student-by-id/delete/{id}")
    public void deleteStudentById(@PathVariable("id") Integer studentId) throws Exception {
        boolean isCalledByAdmin = isAdmin();
        if(isCalledByAdmin){
            studentService.deleteStudent(studentId);
        }
    }

//    Checks whether user is an admin
    private boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        boolean isCalledByAdmin = false;
        for(GrantedAuthority grantedAuthority : securedUser.getAuthorities()){
            String[] authorities = grantedAuthority.getAuthority().split(Constants.DELIMITER);
            isCalledByAdmin = Arrays.stream(authorities).anyMatch(x -> Constants.STUDENT_INFO_AUTHORITY.equals(x));
            if(isCalledByAdmin) break;
        }
        return isCalledByAdmin;
    }


    // Only for student so that they can see their details
    @GetMapping("/student")
    public GetStudentResponse findStudent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();

        return studentService.find(studentId);
    }

//    Student view their issued books
    @GetMapping("/student/book")
    public List<GetBookResponse> getIssuedBooks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();

        return studentService.getMyBooks(studentId);
    }

//    Student update own details
    @PatchMapping("/student/update")
    public void updateDetails(@RequestBody UpdateRequest updateRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();

        studentService.updateDetails(studentId, updateRequest);
    }

//    Student Delete
    @DeleteMapping("/student/delete")
    public void deleteStudent() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();

        studentService.deleteStudent(studentId);
    }
}
