package com.demo.order.handler;

import com.demo.order.exception.ErrorResponse;
import com.demo.order.exception.InvalidInputException;
import com.demo.order.exception.ResourceUnAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public final ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
		return new ResponseEntity(new ErrorResponse("500", ex.getMessage(), ex.getLocalizedMessage()), INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ErrorResponse> handleInvalidInput(InvalidInputException ex) {
		return new ResponseEntity(new ErrorResponse("400", "Please check: " + ex.getAttributeNames().toString(), ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceUnAvailableException.class)
	public ResponseEntity<ErrorResponse> handleResourceUnavailableException(ResourceUnAvailableException ex) {
		return new ResponseEntity(new ErrorResponse("404", "Resource not found", null), NOT_FOUND);
	}

}