package neo.neobis_auth_project.exceptions;

public class BadCredentialForbiddenException extends RuntimeException{
    public BadCredentialForbiddenException(String message) {
        super(message);
    }
}
