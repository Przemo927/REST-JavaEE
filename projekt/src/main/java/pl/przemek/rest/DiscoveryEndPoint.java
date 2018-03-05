package pl.przemek.rest;

import java.io.IOException;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.service.DiscoveryService;
import pl.przemek.wrapper.ResponseMessageWrapper;

@RequestScoped
@Path("/discovery")
public class DiscoveryEndPoint {

	private DiscoveryService discoveryService;
	private HttpServletRequest request;
	private final static ResponseMessageWrapper mw=new ResponseMessageWrapper();

	@Inject
	public DiscoveryEndPoint(DiscoveryService discoveryService, HttpServletRequest request){
		this.discoveryService=discoveryService;
		this.request=request;
	}
	public DiscoveryEndPoint(){}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDiscovery(@Valid Discovery discovery) throws IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session=request.getSession(false);
		User user = (User) session.getAttribute("user");
		discovery.setUser(user);
		discoveryService.addDiscovery(discovery);
		return Response.ok(mw.wrappMessage("Discovery was added")).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") long id) {
		Discovery discovery=discoveryService.getById(id);
		if(discovery==null){
			return Response.ok(mw.wrappMessage("Discovery wasn't found")).build();
		}
		return Response.ok(discovery).build();
	}

    @DELETE
    @Path("/{id}")
    public Response removeById(@PathParam("id") long id) {
        discoveryService.removeDiscoveryById(id);
		return Response.ok("Discovery was removed").header("Access-Control-Allow-Origin","*").build();

    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getALL(@QueryParam("orderBy") @DefaultValue("popular") String order) {
		List<Discovery> allDiscoveries = discoveryService.getAll(order);
	if(allDiscoveries.isEmpty()){
		return Response.ok(mw.wrappMessage("Discoveries weren't found")).build();
	}
		return Response.ok(allDiscoveries).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDiscovery(Discovery discovery) {
		discoveryService.updateDiscovery(discovery);
		return Response.ok(mw.wrappMessage("Discovery was updated")).build();
	}

}
