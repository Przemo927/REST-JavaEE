package pl.przemek.mapper;


import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExceptionMapperAnnotation
@Interceptor
public class ExceptionMapper {

    @Inject
    Logger logger;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ic) throws Exception {
        Method method=ic.getMethod();
        try {
            return ic.proceed();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"["+method.getDeclaringClass().getSimpleName()+"]"+" "+method.getName()+"()",e);
            e.printStackTrace();
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();
        }
    }
}
