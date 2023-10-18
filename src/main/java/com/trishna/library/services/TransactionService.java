package com.trishna.library.services;

import com.trishna.library.dtos.InitiateTransactionRequest;
import com.trishna.library.exceptions.Book.BookNotAvailableException;
import com.trishna.library.exceptions.Book.BookNotFoundException;
import com.trishna.library.exceptions.GeneralException;
import com.trishna.library.exceptions.transaction.LessFineException;
import com.trishna.library.exceptions.transaction.TransactionNotFoundException;
import com.trishna.library.exceptions.user.UserNotFindException;
import com.trishna.library.models.*;
import com.trishna.library.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
//    private TransactionResponse issuance(InitiateTransactionRequest request, Integer adminId) throws Exception {
//        Student student = studentService.findStudent(request.getStudentId());
//        Admin admin = adminService.find(adminId);
//        Book book = bookService.findById(request.getBookId());
//
//        if (student == null){
//            throw new UserNotFindException("Please enter valid Student Id");
//        } else if (book.getStudent() != null) {
//            throw new BookNotAvailableException("Book is not available");
//        }else if(student.getBookList().size() >= maxBooksAllowed){
//            throw new GeneralException("You have already issued 3 books. To issue any other book first return any other book");
//        } else if (book == null) {
//            throw new BookNotFoundException("Please enter valid Book Id");
//        }else if(admin == null) throw new GeneralException("Invalid Request");
//
//        Transaction transaction = null;
//
//        try {
//            transaction = Transaction.builder()
//                    .txnId(UUID.randomUUID().toString())
//                    .student(student)
//                    .book(book)
//                    .admin(admin)
//                    .transactionType(request.getTransactionType())
//                    .transactionStatus(TransactionStatus.PENDING)
//                    .build();
//
//            transactionRepository.save(transaction);
//
//            book.setStudent(student);
//            book.setStatus(BookStatus.NOT_AVAILABLE);
////            bookService.createOrUpdate(book);
//            bookService.update(book);
//            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
//
//        }catch (Exception e){
//            transaction.setTransactionStatus(TransactionStatus.FAILURE);
//        }finally {
//            transactionRepository.save(transaction);
//        }
//        TransactionResponse response = new TransactionResponse(transaction.getTxnId(), transaction.getTransactionStatus());
//
////        return transaction.getTxnId();
//        return response;
//    }

    private TransactionResponse issuance(InitiateTransactionRequest request, Integer adminId) {
        Student student = studentService.findStudent(request.getStudentId());
        Admin admin = adminService.find(adminId);
        Book book = bookService.findById(request.getBookId());

        if (student == null) {
            throw new UserNotFindException("Please enter valid Student Id");
        } else if (book == null) {
            throw new BookNotFoundException("Please enter valid Book Id");
        } else if (book.getQuantity() == 0) {
            throw new BookNotAvailableException("Book is not available");
        } else if(student.getBookList().size() >= maxBooksAllowed) {
            throw new GeneralException("You have already issued 3 books. To issue any other book first return any other book");
        } else if(admin == null) throw new GeneralException("Invalid Request");


        Transaction transaction = null;

        List<Student> studentList = book.getStudentList();
        List<Book> bookList = student.getBookList();
        if(bookList.contains(book)) throw new GeneralException("You have already issued this book");

        try{
            transaction = Transaction.builder()
                    .txnId(UUID.randomUUID().toString())
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .transactionType(request.getTransactionType())
                    .transactionStatus(TransactionStatus.PENDING)
                    .build();

            transactionRepository.save(transaction);





            Integer initialQuantity = book.getQuantity();
            book.setQuantity(initialQuantity - 1);

            bookList.add(book);
            studentList.add(student);
            book.setStudentList(studentList);
            student.setBookList(bookList);
            bookService.update(book);
            studentService.updateBookList(student);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        }catch (GeneralException e){

        }
        catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        }finally {
            transactionRepository.save(transaction);
        }
        TransactionResponse response = new TransactionResponse(transaction.getTxnId(), transaction.getTransactionStatus());

//        return transaction.getTxnId();
        return response;
    }
//    private String returnBook(InitiateTransactionRequest request, Integer adminId) throws Exception {
//    private TransactionResponse returnBook(InitiateTransactionRequest request, Integer adminId) throws Exception {
//        /**
//         * Return
//         * 1. If the book is valid or not and student is valid or not
//         * 2. entry in the Txn table
//         * 3. due date check and fine calculation
//         * 4. if there is no fine, then de-allocate the book from student's name ==> book table
//         */
//
//        Student student = studentService.findStudent(request.getStudentId());
//        Admin admin = adminService.find(adminId);
//
//        Book book = bookService.findById(request.getBookId());
//
//        if (student == null
//                || admin == null // admin is null
//                || book == null
//                || book.getStudent() == null  // if the book is assigned to someone or not
//                || book.getStudent().getId() != student.getId()) { // if the book is assigned to the same student
//            // which is requesting to return or not
//            throw new Exception("Invalid request");
//        }
//
//        // Getting the corresponding issuance txn
//        Transaction issuanceTxn = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(
//                student, book, TransactionType.ISSUE);
//        if(issuanceTxn == null){
//            throw new Exception("Invalid request");
//        }
//
//        Transaction txn = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(student, book, TransactionType.RETURN);
//        if(txn!= null && txn.getTransactionStatus().equals(TransactionStatus.PENDING)){
////            return txn.getTxnId();
//            TransactionResponse response = new TransactionResponse(txn.getTxnId(), txn.getTransactionStatus());
//            return response;
//        }
//
//
//        Transaction transaction = null;
//        try {
//            Integer fine = calculateFine(issuanceTxn.getCreatedOn());
//            transaction = Transaction.builder()
//                    .txnId(UUID.randomUUID().toString())
//                    .student(student)
//                    .book(book)
//                    .admin(admin)
//                    .transactionType(request.getTransactionType())
//                    .transactionStatus(TransactionStatus.PENDING)
//                    .fine(fine)
//                    .build();
//
//            transactionRepository.save(transaction);
//
//            if (fine == 0) {
//                book.setStudent(null);
//                book.setStatus(BookStatus.AVAILABLE);
//                bookService.createOrUpdate(book);
//                transaction.setTransactionStatus(TransactionStatus.SUCCESS);
//            }
//        }catch (Exception e){
//            transaction.setTransactionStatus(TransactionStatus.FAILURE);
//        }finally {
//            transactionRepository.save(transaction);
//        }
//        TransactionResponse response = new TransactionResponse(transaction.getTxnId(), transaction.getTransactionStatus());
////        return transaction.getTxnId();
//        return response;
//    }

    // S1 --> B1 = D1
    // S1 --> B1 = D2
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

        boolean studentExist = false;

        Book book = bookService.findById(request.getBookId());

        if(student == null
                || admin == null
                || book == null){
            throw new Exception("Invalid request");
        }

        List<Student> studentList = book.getStudentList();
        List<Book> bookList = student.getBookList();

        if(!bookList.contains(book)) throw new GeneralException("You have not issued this book");

//        if(studentList.isEmpty()) throw new Exception("Invalid request");
//        for (Student s:
//                studentList) {
//            if(s.getId() == student.getId()){
//                studentExist =true;
//                break;
//            }
//        }
//        if(!studentExist) throw new Exception("Invalid request");

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
        try{
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
                studentList.remove(student);
                bookList.remove(book);
                book.setStudentList(studentList);
                student.setBookList(bookList);
                Integer initialQuantity = book.getQuantity();
                book.setQuantity(initialQuantity + 1);
                bookService.createOrUpdate(book);
                studentService.updateBookList(student);
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
        } else if (returnTxn.getStudent().getId() != studentId) {
            throw new GeneralException("You are not authorized");
        }

        Book book = returnTxn.getBook();

        showFine(txnId, book.getId());

        Student student = studentService.findStudent(studentId);
        List<Student> studentList = book.getStudentList();
        List<Book> bookList = student.getBookList();

//        if(returnTxn.getFine() == amount && book.getStudent() != null && book.getStudent().getId() == studentId){
//            returnTxn.setTransactionStatus(TransactionStatus.SUCCESS);
//            book.setStudent(null);
//            bookService.createOrUpdate(book);
//            transactionRepository.save(returnTxn);
//            TransactionResponse response = new TransactionResponse(returnTxn.getTxnId(), returnTxn.getTransactionStatus());
//            return response;
//        } else if (returnTxn.getFine() != amount) {
//            throw new LessFineException("Check your fine amount");
//        } else{
//            throw new GeneralException("Invalid request");
//        }
        if(returnTxn.getFine() == amount && !studentList.isEmpty() && studentList.contains(student)){
            returnTxn.setTransactionStatus(TransactionStatus.SUCCESS);
            studentList.remove(student);
            bookList.remove(book);
            book.setStudentList(studentList);
            student.setBookList(bookList);
            Integer initialQuantity = book.getQuantity();
            book.setQuantity(initialQuantity + 1);
            bookService.createOrUpdate(book);
            studentService.updateBookList(student);
            transactionRepository.save(returnTxn);
            TransactionResponse response = new TransactionResponse(returnTxn.getTxnId(), returnTxn.getTransactionStatus());
            return response;
        }else if (returnTxn.getFine() != amount) {
            throw new LessFineException("Check your fine amount");
        } else{
            throw new GeneralException("Invalid request");
        }

    }

    public void deleteTransaction(Student student){


    }
}
