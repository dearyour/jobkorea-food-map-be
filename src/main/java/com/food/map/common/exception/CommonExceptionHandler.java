package com.food.map.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
		Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
		String errMsg = constraintViolations
				.stream()
				.map(v -> String.format("%s 필드는 %s", v.getPropertyPath(), v.getMessage()))
				.collect(Collectors.joining(", "));

		log.error("ConstraintViolationException Exception::{}", errMsg);
		return ApiErrorResponse.toResponse(HttpStatus.BAD_REQUEST, errMsg);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ApiErrorResponse> processValidationError(MethodArgumentNotValidException exception) {
		List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
		String errMsg = allErrors
				.stream()
				.map(v -> (FieldError)v)
				.map(v -> String.format("%s 필드는 %s", v.getField(), v.getDefaultMessage()))
				.collect(Collectors.joining(","));

		log.error("MethodArgumentNotValidException Exception::{}", errMsg);
		return ApiErrorResponse.toResponse(HttpStatus.BAD_REQUEST, errMsg);
	}

	@ExceptionHandler({BusinessErrorCodeException.class})
	public ResponseEntity<ApiErrorResponse> handleBusinessErrorCodeException(BusinessErrorCodeException exception) {
		log.error("BusinessErrorCodeException Exception::{}::{}", exception.getErrorCode(), exception.getMessage());
		return ApiErrorResponse.toResponse(exception.getErrorCode());
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ApiErrorResponse> handleOtherException(Exception exception) {
		log.error("Other Exception::{}", exception.getMessage());
		return ApiErrorResponse.toResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}
}
