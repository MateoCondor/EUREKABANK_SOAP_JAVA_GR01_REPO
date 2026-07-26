package ec.edu.monster.exception;

import ec.edu.monster.dto.ErrorResponseDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TransactionExceptionMapper implements ExceptionMapper<TransactionException> {
    @Override
    public Response toResponse(TransactionException exception) {
        return Response.status(exception.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponseDTO(exception.getMessage()))
                .build();
    }
}
