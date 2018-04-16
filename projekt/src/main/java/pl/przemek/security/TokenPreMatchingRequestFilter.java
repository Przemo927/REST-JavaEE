package pl.przemek.security;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class TokenPreMatchingRequestFilter implements ContainerRequestFilter {


	@Inject
    private TokenStore tokenStore;
    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (requestContext.getSecurityContext().isUserInRole("admin") || requestContext.getSecurityContext().isUserInRole("user")) {
        	String token =tokenStore.getToken();
        	requestContext.getHeaders().add(AUTHORIZATION, token);
        	
        }
            }
    }
