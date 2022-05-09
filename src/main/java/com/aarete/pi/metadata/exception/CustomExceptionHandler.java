package com.aarete.pi.metadata.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = Exception.class)
    public final ResponseEntity<Object> exception(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        return new ResponseEntity<Object>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(value = RecordNotFound.class)
    public final ResponseEntity<Object> exception(RecordNotFound ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Record Not Found", details);
        return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = AccessDenied.class)
    public final ResponseEntity<Object> exception(AccessDenied ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Access Denied", details);
        return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
    }
}
