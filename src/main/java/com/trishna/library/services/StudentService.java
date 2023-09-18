package com.trishna.library.services;

import com.trishna.library.dtos.GetBookResponse;
import com.trishna.library.dtos.GetStudentResponse;
import com.trishna.library.dtos.UpdateRequest;
import com.trishna.library.models.Book;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.models.Student;
import com.trishna.library.models.Transaction;
import com.trishna.library.repositories.StudentCacheRepository;
import com.trishna.library.repositories.StudentRepository;
import com.trishna.library.repositories.TransactionRepository;
import com.trishna.library.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StudentCacheRepository studentCacheRepository;
    @Autowired
    UserService userService;
    @Autowired
    TransactionRepository transactionRepository;
    public void create(Student student) {
        SecuredUser securedUser = student.getSecuredUser();
        securedUser = userService.save(securedUser, Constants.STUDENT_USER);
        student.setSecuredUser(securedUser);
        studentRepository.save(student);
//        GetStudentResponse response = student1.to();
//        return response;
    }

    public GetStudentResponse find(Integer studentId) {
        GetStudentResponse student = studentCacheRepository.get(studentId);
        if(student != null)
            return student;
        student = studentRepository.findById(studentId).orElse(null).to();
        if(student != null)
            studentCacheRepository.set(student);
        return student;
    }

    public Student findStudent(int studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    public List<GetBookResponse> getMyBooks(int studentId) {
        List<Book> list = studentRepository.findById(studentId).get().getBookList();
        List<GetBookResponse> ans = new ArrayList<>();
        if(list.size() == 0) return new ArrayList<>();
        for(int i = 0; i<list.size(); i++){
            ans.add(list.get(i).to());
        }
        return ans;
    }

    public void updateDetails(int studentId, UpdateRequest request) throws Exception {
        Student retrievdStudent = studentRepository.findById(studentId).orElse(null);
        String key = request.getUpdateKey();
        String value = request.getUpdateValue();
        if(retrievdStudent != null){
            switch (key){
                case "name" : {
                    retrievdStudent.setName(value);
                    break;
                }
                case "email" : {
                    retrievdStudent.setEmail(value);
                    break;
                }
                case "age" : {
                    retrievdStudent.setAge(Integer.parseInt(value));
                    break;
                }
                default:
                    throw new Exception("Invalid update key");
            }
            studentRepository.save(retrievdStudent);
            studentCacheRepository.getSet(retrievdStudent.getId(), retrievdStudent.to());
        }
        else throw new Exception("Student Not found");
    }

    public void deleteStudent(Integer studentId) throws Exception {
        Student retrievedStudent = studentRepository.findById(studentId).orElse(null);
        if(retrievedStudent != null && retrievedStudent.getBookList().size() == 0){
            studentCacheRepository.delete(studentId);
            List<Transaction> transactionList = transactionRepository.findByStudent(retrievedStudent);
            for (Transaction txn: transactionList) {
                transactionRepository.deleteById(txn.getId());
            }
            studentRepository.deleteById(studentId);

            userService.deleteById(retrievedStudent.getSecuredUser().getId());

        }
        else if(retrievedStudent != null && retrievedStudent.getBookList().size() > 0)
            throw new Exception("Student didn't return all books");
    }
}
