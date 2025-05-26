package com.example.ecommerce_project.exceptions;

import com.example.ecommerce_project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    //if the criteria of the method is not valid, let say categoryName cannot be null
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField(); //the field is "categoryName" this code is getting the field of API that we build
            String message = err.getDefaultMessage(); //the message is the error message
            response.put(fieldName, message);
        });
        //ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);, but can be replaced to be ResponseEntity<>(response ....)
        //ResponseEntity is for error code API 200, 300, 400, 500
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse response = new APIResponse(message, false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        String message = e.getMessage();
        APIResponse response = new APIResponse(message, false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
}
