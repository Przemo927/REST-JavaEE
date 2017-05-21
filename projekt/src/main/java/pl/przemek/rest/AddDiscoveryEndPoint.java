package pl.przemek.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.DiscoveryRepository;

@Path("/add")
public class AddDiscoveryEndPoint {

	@Inject
	private DiscoveryRepository discrepo;
	@Inject 
	HttpServletRequest request;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void AddDiscovery(Discovery discovery) throws IOException{
		request.setCharacterEncoding("UTF-8");
		User user=(User) request.getSession().getAttribute("user");
		discovery.setUser(user);
		discovery.setTimestamp(new Timestamp(new Date().getTime()));
		discovery.setUpVote(0);
		discovery.setUpVote(0);
		discrepo.add(discovery);
		
		
	}
}
