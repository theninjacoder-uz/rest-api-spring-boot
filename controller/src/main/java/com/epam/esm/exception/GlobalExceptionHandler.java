package com.epam.esm.exception;

import com.epam.esm.dto.response.AppErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppErrorResponseDto( 1803,
                        Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppErrorResponseDto> handleResourceAlreadyExistException(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppErrorResponseDto(1804, e.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<AppErrorResponseDto> handleResourceAlreadyExistException(ResourceAlreadyExistException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AppErrorResponseDto(1805, e.getMessage()));
    }

    @ExceptionHandler(DataPersistenceException.class)
    public ResponseEntity<AppErrorResponseDto> handleDataPersistenceException(DataPersistenceException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AppErrorResponseDto(1806, ex.getMessage()));
    }

}
