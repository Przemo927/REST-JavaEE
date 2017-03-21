package pl.przemek.rest;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import pl.przemek.model.Discovery;
import pl.przemek.repository.DiscoveryRepository;

@Path("/home")
public class HomeEndPoint {

@Inject
private DiscoveryRepository discrepo;

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
public boolean login(@Context HttpServletRequest request){
	Boolean answer=false;
	if(request.getUserPrincipal() != null){
		answer=true;
	}
	return answer;
	
}
}
