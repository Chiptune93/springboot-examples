package dev.chiptune.springboot.error;

public class RestCustomNotFoundException extends Exception {
    public RestCustomNotFoundException(String message) {
        super("Rest Custom Not Found!\n" + message);
    }
}
