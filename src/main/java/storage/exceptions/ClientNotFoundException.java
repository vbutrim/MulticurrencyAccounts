package storage.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super(String.format("Client with id '%s' not found", id));
    }

    public ClientNotFoundException(String name) {
        super(String.format("Client with name '%s' not found", name));
    }
}
