package pl.przemek.rest;

import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.model.Connection;
import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.DiscoveryRepository;
@RequestScoped
@Path("/home")
public class HomeEndPoint {

@Inject
private DiscoveryRepository discrepo;
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
@GET
@Path("/check")
@Produces(MediaType.APPLICATION_JSON)
public Connection login (@Context HttpServletRequest request){
	Connection con= new Connection();
	con.setName("Zaloguj");
	con.setPath("/projekt/index.html#!/login");
	if(request.getUserPrincipal() != null){
		con.setName("Wyloguj");
		con.setPath("/projekt/api/logout");	
	}
	return con;
}
@GET
@Path("/{name}")
@Produces(MediaType.APPLICATION_JSON)
public List<Discovery> getByName(@PathParam ("name") String name){
	List<Discovery> discovery=discrepo.getByName(name);
	return discovery;
	
}
}
