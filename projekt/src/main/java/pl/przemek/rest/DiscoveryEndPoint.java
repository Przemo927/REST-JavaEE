package pl.przemek.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.DiscoveryRepository;
import pl.przemek.service.DiscoveryService;

@RequestScoped
@Path("/discovery")
public class DiscoveryEndPoint {

	@Inject
	private DiscoveryService discoveryService;
	@Inject
	HttpServletRequest request;
	Discovery discovery;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void AddDiscovery(@Valid Discovery discovery) throws IOException {
		request.setCharacterEncoding("UTF-8");
		User user = (User) request.getSession().getAttribute("user");
		discovery.setUser(user);
		discoveryService.addDiscovery(discovery);
	}

	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Discovery getByName(@PathParam("name") String name) {
		return discoveryService.getByName(name);


	}

    @DELETE
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeByName(@PathParam("name") String name) {
        discoveryService.removeDiscoveryByName(name);

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
