package dev.chiptune.springboot.error.response;

import lombok.Getter;

// 에러 디테일을 담을 클래스
@Getter
public class ErrorDetails {
    // getter 메소드들
    private int statusCode;
    private String message;
    private String details;

    public ErrorDetails(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

}
