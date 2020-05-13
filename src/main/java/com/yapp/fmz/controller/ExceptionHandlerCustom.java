package com.yapp.fmz.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerCustom {




   // Parameter 데이터 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("code",400);
        response.put("message", "파라미터 타입 불일치입니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 지원하지 않는 HTTP method 호출 할 경우 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("code",400);
        response.put("message", "지원하지 않는 HTTP Method 입니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

   // Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("code",400);
        response.put("message", "권한이 부여되지 않은 요청입니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity exceptionHandler(Exception e){
        log.error("UnknownException", e);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("code",400);
        response.put("message", "통신에 실패했습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
}