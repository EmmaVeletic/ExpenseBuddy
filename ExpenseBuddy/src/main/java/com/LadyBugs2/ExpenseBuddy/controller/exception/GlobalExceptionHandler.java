package com.LadyBugs2.ExpenseBuddy.controller.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.LadyBugs2.ExpenseBuddy.models.dto.Error;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/*@ExceptionHandler(RecordNotFoundException.class)

	public ResponseEntity<Error> handleRecordExceptionNotFound(RecordNotFoundException ex, WebRequest request){

		Error errorObject = new Error();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		return new ResponseEntity<Error>(errorObject, HttpStatus.NOT_FOUND);
	}*/
	
	@ExceptionHandler(RecordNotFoundException.class)
    public String handleRecordNotFoundException(HttpServletRequest request, RecordNotFoundException ex, Model model) {
        model.addAttribute("notFound", true);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("time", new Date());
        return "response";
    }


	/*@ExceptionHandler(MethodArgumentTypeMismatchException.class)

	public ResponseEntity<Error> handleMethodArgumentTypeMismatchException(RecordNotFoundException ex, WebRequest request){

		Error errorObject = new Error();
		errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		return new ResponseEntity<Error>(errorObject, HttpStatus.BAD_REQUEST);
	}*/
	
	@ExceptionHandler(Exception.class)
	
    public String handleGlobalException(HttpServletRequest request, Exception ex, Model model) {
        model.addAttribute("serverError", true);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("time", new Date());
        model.addAttribute("stackTrack", ex.getStackTrace());
        return "response";
    }

	/*@ExceptionHandler(Exception.class)

	public ResponseEntity<Error> handleGeneralException(RecordNotFoundException ex, WebRequest request){

		Error errorObject = new Error();
		errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		return new ResponseEntity<Error>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
	}*/

	@ExceptionHandler(ItemAlreadyExistsException.class)

	public ResponseEntity<Error> handleItemAlreadyExistsException(ItemAlreadyExistsException ex, WebRequest request){

		Error errorObject = new Error();
		errorObject.setStatusCode(HttpStatus.CONFLICT.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		return new ResponseEntity<Error>(errorObject, HttpStatus.CONFLICT);
	}

	/*@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("statusCode", HttpStatus.BAD_REQUEST.value());

		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("messages", errors);

		body.put("timestamp", new Date());

		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);

	}*/
}