package pl.przemek.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.DiscoveryRepository;

@Path("/discovery")
public class DiscoveryEndPoint {

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
	
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Discovery> getByName(@PathParam ("name") String name){
		List<Discovery> discovery=discrepo.getByName(name);
		return discovery;
		
	}
	
	@RequestScoped
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Discovery> getALL(@QueryParam("orderBy") @DefaultValue("popular") String order){
		List<Discovery> discoveries = null;
		if(order.equals("popular")){
		discoveries=discrepo.getAll(new Comparator<Discovery>() {
			 @Override
			public int compare(Discovery d1, Discovery d2) {
	            int d1Vote = d1.getUpVote() - d1.getDownVote();
	            int d2Vote = d2.getUpVote() - d2.getDownVote();
	            if(d1Vote < d2Vote) {
	                return 1;
	            } else if(d1Vote > d2Vote) {
	                return -1;
	            }
	            return 0;
	        }});
		return discoveries;
	}
		
		else if(order.equals("time")){
		discoveries=discrepo.getAll(new Comparator<Discovery>() {
	            @Override
	            public int compare(Discovery d1, Discovery d2) {
	           return d1.getTimestamp().compareTo(d2.getTimestamp());
		}});
		}
		return discoveries;
		}
}
