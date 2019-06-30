package controllers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    // TODO: handle exceptions in more pretty way
    @Override
    public Response toResponse(Throwable exception) {
        return null;
    }
}
