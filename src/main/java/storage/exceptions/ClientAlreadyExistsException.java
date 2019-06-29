package storage.exceptions;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String name) {
        super(String.format("Client with name '%s' already exists", name));
    }
}
