package pl.przemek.mapper;

import java.util.*;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.api.validation.ResteasyViolationException;
import org.json.simple.JSONObject;

@Provider
public class Mapper implements ExceptionMapper<ValidationException> {

	
JSONObject json;
Map<String,String> map;

    @Override
    public Response toResponse(ValidationException e) {
    	json=new JSONObject();
    	map=new HashMap<>();
    	if(e instanceof ResteasyViolationException) {
            ResteasyViolationException re = (ResteasyViolationException) e;
            Set<ConstraintViolation<?>> violations = re.getConstraintViolations();
            for (ConstraintViolation<?> v : violations) {
                String[] strings = (v.getPropertyPath() + "").split("\\.");
                map.put(strings[2],v.getMessage());
        json.put("InvalidFieldList",map);
        }
            return Response.status(Status.BAD_REQUEST).entity(json).type(MediaType.APPLICATION_JSON).build();
    	}
		return Response.status(Status.BAD_REQUEST).entity("sorry").type(MediaType.APPLICATION_JSON).build();
        
    }
}
