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
import pl.przemek.service.DiscoveryService;

@Path("/discovery")
public class DiscoveryEndPoint {

	@Inject
	private DiscoveryService discoveryService;
	@Inject
	HttpServletRequest request;
	Discovery discovery;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void AddDiscovery(Discovery discovery) throws IOException {
		request.setCharacterEncoding("UTF-8");
		User user = (User) request.getSession().getAttribute("user");
		discovery.setUser(user);
		discoveryService.addDiscovery(discovery);
	}

	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Discovery> getByName(@PathParam("name") String name) {
		List<Discovery> discoveries = discoveryService.getByName(name);
		return discoveries;

	}

	@RequestScoped
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Discovery> getALL(@QueryParam("orderBy") @DefaultValue("popular") String order) {
		List<Discovery> allDiscoveries = discoveryService.getAll(order);
		return allDiscoveries;
	}
}
