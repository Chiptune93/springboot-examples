package dev.chiptune.springboot.config.errorhandler;


import dev.chiptune.springboot.error.CustomNotFoundException;
import dev.chiptune.springboot.error.RestCustomNotFoundException;
import dev.chiptune.springboot.error.response.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomRestControllerAdvice {

    // 사용자 정의 예외 핸들링
    @ExceptionHandler(RestCustomNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            CustomNotFoundException ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
