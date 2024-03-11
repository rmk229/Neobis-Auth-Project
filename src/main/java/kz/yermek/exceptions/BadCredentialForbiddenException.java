package kz.yermek.exceptions;

public class BadCredentialForbiddenException extends RuntimeException{
    public BadCredentialForbiddenException(String message) {
        super(message);
    }
}
