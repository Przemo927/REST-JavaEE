package pl.przemek.rest.utils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

public class ResponseUtils {

    private final static String ROOT_REST_PATH="/api";
    private final static String LOGOUT_PATH="/logout";

    private static CacheControl cacheControl;
    private static String homePath;

    public final static String URL_SEPARATOR="/";

    public static CacheControl getCacheControl(int maxAge){
        if(cacheControl==null){
            cacheControl=new CacheControl();
        }
        cacheControl.setMaxAge(maxAge);
        return cacheControl;
    }

    public static <T> Response.ResponseBuilder checkIfModifiedAndReturnResponse(T element, Request request){
        EntityTag eTag=new EntityTag(element.hashCode()+"");
        Response.ResponseBuilder responseBuilder=request.evaluatePreconditions(eTag);
        if(responseBuilder!=null) {
            return responseBuilder.tag(eTag);
        }
        else {
            return Response.ok(element).tag(eTag);
        }
    }

    public static String getHomePath(HttpServletRequest request){
        if(homePath==null)
            homePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        return homePath;
    }

    public static String getLogoutPath(HttpServletRequest request){
        return getHomePath(request)+ROOT_REST_PATH+LOGOUT_PATH;
    }
}
