package pl.przemek.rest;

import java.io.IOException;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.service.DiscoveryService;

@RequestScoped
@Path("/discovery")
public class DiscoveryEndPoint {

	private DiscoveryService discoveryService;
	@Inject
	HttpServletRequest request;

	@Inject
	public DiscoveryEndPoint(DiscoveryService discoveryService){
		this.discoveryService=discoveryService;
	}
	public DiscoveryEndPoint(){}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void AddDiscovery(@Valid Discovery discovery) throws IOException {
		request.setCharacterEncoding("UTF-8");
		User user = (User) request.getSession().getAttribute("user");
		discovery.setUser(user);
		discoveryService.addDiscovery(discovery);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Discovery getByName(@PathParam("id") long id) {
		return discoveryService.getById(id);


	}

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeById(@PathParam("id") long id) {
        discoveryService.removeDiscoveryById(id);

    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Discovery> getALL(@QueryParam("orderBy") @DefaultValue("popular") String order) {
		List<Discovery> allDiscoveries = discoveryService.getAll(order);
		return allDiscoveries;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateDiscovery(Discovery discovery){
		discoveryService.updateDiscovery(discovery);
	}
}
