package neo.neobis_auth_project.exceptions;

public class BadCredentialException extends RuntimeException {
    public BadCredentialException(String s) {
        super(s);
    }
}