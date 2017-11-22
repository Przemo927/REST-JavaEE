package pl.przemek.filter;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepository;


@WebFilter("/*")
public class LoginFilter implements Filter {
	
	@Inject
	JpaUserRepository userrep;

	  @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	        HttpServletRequest httpReq = (HttpServletRequest) request;
		  HttpServletResponse httpResp = (HttpServletResponse) response;
	        if(httpReq.getUserPrincipal() != null && httpReq.getSession().getAttribute("user") == null) {
				String username = httpReq.getUserPrincipal().getName();
				List<User> listUserByUsername = userrep.getUserByUsername(username);
				User userByUsername=listUserByUsername.get(0);
				LogoutIfInActiveStatus(userByUsername,httpReq,httpResp);
	            saveUserInSession(httpReq,userByUsername);
	        }
	        chain.doFilter(request, response);
	    }
	    void LogoutIfInActiveStatus(User user,HttpServletRequest request,HttpServletResponse response) throws IOException {
			if(!user.isActive()){
				request.getSession().invalidate();
				response.sendRedirect("http://localhost:8080/projekt/index.html#/");
			}
		}
	    void saveUserInSession(HttpServletRequest request,User user) {

	        request.getSession(true).setAttribute("user", user);
	    }
	 
	    @Override
	    public void init(FilterConfig filterConfig) throws ServletException {
	    }
	 
	    @Override
	    public void destroy() {
	    }
	}
