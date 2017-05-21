package pl.przemek.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import pl.przemek.model.User;
import pl.przemek.repository.UserRepository;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {
	
	@Inject 
	UserRepository userrep;

	  @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	        HttpServletRequest httpReq = (HttpServletRequest) request;
	        if(httpReq.getUserPrincipal() != null && httpReq.getSession().getAttribute("user") == null) {
	            saveUserInSession(httpReq);
	        }
	        chain.doFilter(request, response);
	    }
	 
	    private void saveUserInSession(HttpServletRequest request) {
	        String username = request.getUserPrincipal().getName();
	        User userByUsername = userrep.getUserByUsername(username);
	        request.getSession(true).setAttribute("user", userByUsername);
	    }
	 
	    @Override
	    public void init(FilterConfig filterConfig) throws ServletException {
	    }
	 
	    @Override
	    public void destroy() {
	    }
	}
