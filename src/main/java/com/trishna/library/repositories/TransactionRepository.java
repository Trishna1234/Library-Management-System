package com.trishna.library.repositories;

import com.trishna.library.models.Book;
import com.trishna.library.models.Student;
import com.trishna.library.models.Transaction;
import com.trishna.library.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

//    @Query("select * from transaction where student_id = ?1 and book_id = ?2 and transactionType = ?3 order by id desc limit 1")
    Transaction findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(Student student, Book book, TransactionType transactionType);

    Transaction findByTxnId(String txnId);

    List<Transaction> findByStudent(Student student);

    List<Transaction> findByBook(Book book);

    // S1 --> B1 (Issuance) t1
    // s1 --> b1 (return) t2
    // s1 --> b1 (issue) t3
    // s1 --> b1 (return) t4
}
