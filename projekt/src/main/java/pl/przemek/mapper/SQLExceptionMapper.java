package pl.przemek.mapper;

import org.jboss.resteasy.api.validation.ResteasyViolationException;

import javax.ejb.ApplicationException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Provider
public class SQLExceptionMapper implements ExceptionMapper<EJBTransactionRolledbackException> {

    @Override
    public Response toResponse(EJBTransactionRolledbackException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Wybierz inną nazwę").type(MediaType.APPLICATION_JSON).build();
    }
}
