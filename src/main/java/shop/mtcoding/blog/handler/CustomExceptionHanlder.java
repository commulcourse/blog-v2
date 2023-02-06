package shop.mtcoding.blog.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.util.Script;

@RestControllerAdvice
public class CustomExceptionHanlder {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customException(CustomException e) {

        String responsbody = Script.back(e.getMessage());
        return new ResponseEntity<>(responsbody, e.getStatus()); // body, 상태값
    }
}
// @REstControllerAdvice, ResponseEntity - 데이터를 리턴할 때 쓴다.
