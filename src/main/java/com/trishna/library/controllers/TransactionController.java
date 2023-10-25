package com.trishna.library.controllers;

import com.trishna.library.dtos.InitiateTransactionRequest;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.models.TransactionResponse;
import com.trishna.library.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public TransactionResponse initiateTxn(@RequestBody @Valid InitiateTransactionRequest transactionRequest) throws Exception {

        // student id :
        // book id
        // admin id
        // txn type

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        Integer adminId = securedUser.getAdmin().getId();

        return transactionService.initiateTxn(transactionRequest, adminId);
    }


    @PostMapping("/transaction/payment")
    public TransactionResponse makePayment(@RequestParam("amount") Integer amount,
                            @RequestParam("transactionId") String txnId) throws Exception {
        return transactionService.payFine(amount, getStudentId(), txnId);
    }

    @GetMapping("/transaction/pending")
    public List<TransactionResponse> showPendingTxn(){
        return transactionService.showPendingTransaction(getStudentId());
    }

    @GetMapping("/transaction/show-fine")
    public Integer showFine(@RequestParam("txnId") String txnId,
                            @RequestParam("bookId") Integer bookId){
        return transactionService.showFine(txnId, bookId);
    }

    private Integer getStudentId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        return securedUser.getStudent().getId();
    }
}
