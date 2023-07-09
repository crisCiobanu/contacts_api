package tech.cristianciobanu.contactsapi.auth.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message){
        super(message);
    }
}
