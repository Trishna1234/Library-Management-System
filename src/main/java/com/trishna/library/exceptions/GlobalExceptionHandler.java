package com.trishna.library.exceptions;

import com.trishna.library.exceptions.transaction.LessFineException;
import com.trishna.library.exceptions.transaction.TransactionNotFoundException;
import com.trishna.library.exceptions.user.UserAlreadyExistsException;
import com.trishna.library.exceptions.user.UserNotFindException;
import com.trishna.library.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> studentAlreadyExists(UserAlreadyExistsException userAlreadyExistsException){
        ErrorResponse response = new ErrorResponse(userAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFindException.class)
    public ResponseEntity<ErrorResponse> userNotFind(UserNotFindException userNotFindException){
        ErrorResponse response = new ErrorResponse(userNotFindException.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> txnNotFind(TransactionNotFoundException transactionNotFoundException){
        ErrorResponse response = new ErrorResponse(transactionNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(LessFineException.class)
    public ResponseEntity<ErrorResponse> lessFine(LessFineException lessFineException){
        ErrorResponse response = new ErrorResponse(lessFineException.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponse> generalException(GeneralException generalException){
        ErrorResponse response = new ErrorResponse(generalException.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
