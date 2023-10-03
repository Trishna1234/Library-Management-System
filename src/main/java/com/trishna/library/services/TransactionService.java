package com.trishna.library.services;

import com.trishna.library.dtos.InitiateTransactionRequest;
import com.trishna.library.exceptions.GeneralException;
import com.trishna.library.exceptions.transaction.LessFineException;
import com.trishna.library.exceptions.transaction.TransactionNotFoundException;
import com.trishna.library.models.*;
import com.trishna.library.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    AdminService adminService;

    @Autowired
    BookService bookService;

    @Value("${student.allowed.max-books}") // from application.properties
    Integer maxBooksAllowed;

    @Value("${student.allowed.duration}")
    Integer duration;

    public TransactionResponse initiateTxn(InitiateTransactionRequest request, Integer adminId) throws Exception {
        /**
         * Issuance
         * 1. If the book is available or not and student is valid or not
         * 2. entry in the Txn
         * 3. we need to check if student has reached the maximum limit of issuance
         * 4. book to be assigned to a student ==> update in book table
         *
         */

        /**
         * Return
         * 1. If the book is valid or not and student is valid or not
         * 2. entry in the Txn table
         * 3. due date check and fine calculation
         * 4. if there is no fine, then de-allocate the book from student's name ==> book table
         */
//        String txnId = request.getTransactionType() == TransactionType.ISSUE
//                ? issuance(request, adminId) :
//                returnBook(request, adminId);
//        TransactionResponse response = new TransactionResponse(txnId, )
        return request.getTransactionType() == TransactionType.ISSUE
                ? issuance(request, adminId) :
                returnBook(request, adminId);
    }

//    private String issuance(InitiateTransactionRequest request, Integer adminId) throws Exception {
    private TransactionResponse issuance(InitiateTransactionRequest request, Integer adminId) throws Exception {
        Student student = studentService.findStudent(request.getStudentId());
        Admin admin = adminService.find(adminId);
        Book book = bookService.findById(request.getBookId());

        if (student == null
                || admin == null
                || book == null
                || student.getBookList().size() >= maxBooksAllowed) {
            throw new GeneralException("Invalid request");
        } else if (book.getStudent() != null) {
            throw new GeneralException("Book is not available");
        }

        Transaction transaction = null;

        try {
            transaction = Transaction.builder()
                    .txnId(UUID.randomUUID().toString())
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .transactionType(request.getTransactionType())
                    .transactionStatus(TransactionStatus.PENDING)
                    .build();

            transactionRepository.save(transaction);

            book.setStudent(student);
            book.setStatus(BookStatus.NOT_AVAILABLE);
//            bookService.createOrUpdate(book);
            bookService.update(book);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        }catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        }finally {
            transactionRepository.save(transaction);
        }
        TransactionResponse response = new TransactionResponse(transaction.getTxnId(), transaction.getTransactionStatus());

//        return transaction.getTxnId();
        return response;
    }

//    private String returnBook(InitiateTransactionRequest request, Integer adminId) throws Exception {
    private TransactionResponse returnBook(InitiateTransactionRequest request, Integer adminId) throws Exception {
        /**
         * Return
         * 1. If the book is valid or not and student is valid or not
         * 2. entry in the Txn table
         * 3. due date check and fine calculation
         * 4. if there is no fine, then de-allocate the book from student's name ==> book table
         */

        Student student = studentService.findStudent(request.getStudentId());
        Admin admin = adminService.find(adminId);

        Book book = bookService.findById(request.getBookId());

        if (student == null
                || admin == null // admin is null
                || book == null
                || book.getStudent() == null  // if the book is assigned to someone or not
                || book.getStudent().getId() != student.getId()) { // if the book is assigned to the same student
            // which is requesting to return or not
            throw new Exception("Invalid request");
        }

        // Getting the corresponding issuance txn
        Transaction issuanceTxn = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(
                student, book, TransactionType.ISSUE);
        if(issuanceTxn == null){
            throw new Exception("Invalid request");
        }

        Transaction txn = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(student, book, TransactionType.RETURN);
        if(txn!= null && txn.getTransactionStatus().equals(TransactionStatus.PENDING)){
//            return txn.getTxnId();
            TransactionResponse response = new TransactionResponse(txn.getTxnId(), txn.getTransactionStatus());
            return response;
        }


        Transaction transaction = null;
        try {
            Integer fine = calculateFine(issuanceTxn.getCreatedOn());
            transaction = Transaction.builder()
                    .txnId(UUID.randomUUID().toString())
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .transactionType(request.getTransactionType())
                    .transactionStatus(TransactionStatus.PENDING)
                    .fine(fine)
                    .build();

            transactionRepository.save(transaction);

            if (fine == 0) {
                book.setStudent(null);
                book.setStatus(BookStatus.AVAILABLE);
                bookService.createOrUpdate(book);
                transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            }
        }catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        }finally {
            transactionRepository.save(transaction);
        }
        TransactionResponse response = new TransactionResponse(transaction.getTxnId(), transaction.getTransactionStatus());
//        return transaction.getTxnId();
        return response;
    }

    // S1 --> B1 = D1
    // S1 --> B1 = D2

    private Integer calculateFine(Date issuanceTime){

        long issueTimeInMillis = issuanceTime.getTime();
        long currentTime = System.currentTimeMillis();

        long diff = currentTime - issueTimeInMillis;

        long daysPassed = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        if(daysPassed > duration){
            return (int)(daysPassed - duration);
        }

        return 0;
    }

    public Integer showFine(String txnId, Integer bookId){

//      GETTING THE RETURN TRANSACTION
        Transaction returnTxn = transactionRepository.findByTxnId(txnId);

//      GETTING THE STUDENT WHO REQUESTED THE RETURN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        Student student = studentService.findStudent(studentId);

//      GETTING THE BOOK FOR WHICH RETURN IS REQUESTED
        Book book = bookService.findById(bookId);

//      GETTING THE ISSUE OF TRANSACTION OF THE PARTICULAR BOOK
        Transaction issuanceTxn = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(
                student, book, TransactionType.ISSUE);

//      CALCULATE THE CURRENT FINE
        int fine = calculateFine(issuanceTxn.getCreatedOn());
        returnTxn.setFine(fine);
        transactionRepository.save(returnTxn);
        return fine;
    }

    public TransactionResponse payFine(Integer amount, Integer studentId, String txnId) throws Exception {

        Transaction returnTxn = transactionRepository.findByTxnId(txnId);

        if(returnTxn == null){
            throw new TransactionNotFoundException("Check your transaction Id");
        }

        Book book = returnTxn.getBook();

        showFine(txnId, book.getId());

        if(returnTxn.getFine() == amount && book.getStudent() != null && book.getStudent().getId() == studentId){
            returnTxn.setTransactionStatus(TransactionStatus.SUCCESS);
            book.setStudent(null);
            bookService.createOrUpdate(book);
            transactionRepository.save(returnTxn);
            TransactionResponse response = new TransactionResponse(returnTxn.getTxnId(), returnTxn.getTransactionStatus());
            return response;
        } else if (returnTxn.getFine() != amount) {
            throw new LessFineException("Check your fine amount");
        } else{
            throw new GeneralException("Invalid request");
        }

    }

    public void deleteTransaction(Student student){


    }
}
