package dev.chiptune.springboot.error;

public class CustomNotFoundException extends Exception {
    public CustomNotFoundException(String message) {
        super("Custom Not Found!\n" + message);
    }
}
