package pl.przemek.mapper;

import org.jboss.resteasy.api.validation.ResteasyViolationException;
import org.json.simple.JSONObject;
import pl.przemek.Utils.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Provider
@SuppressWarnings("unchecked")
public class ValidatorMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException e) {
    	if(e instanceof ResteasyViolationException) {
            ResteasyViolationException re = (ResteasyViolationException) e;
            Set<ConstraintViolation<?>> violations = re.getConstraintViolations();
            Map<String, String> map = new HashMap<>();
            JSONObject json = new JSONObject();
            for (ConstraintViolation<?> v : violations) {
                String[] strings = (v.getPropertyPath().toString()).split("\\"+StringUtils.DOT);
                map.put(strings[2],v.getMessage());
                json.put("invalidFieldList", map);
            }
            return Response.status(Status.BAD_REQUEST).entity(json).build();
    	}
		return Response.status(Status.BAD_REQUEST).build();
        
    }
}
