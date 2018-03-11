package pl.przemek.rest;

import java.io.IOException;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
	@Context
	UriInfo uriInfo;

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
		return Response.created(URI.create(uriInfo.getAbsolutePath()+"/"+discovery.getId())).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") long id) {
		Discovery discovery=discoveryService.getById(id);
		if(discovery==null){
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		System.out.println(uriInfo.getAbsolutePath());
		return Response.ok(discovery).build();
	}

    @DELETE
    @Path("/{id}")
    public Response removeById(@PathParam("id") long id) {
        discoveryService.removeDiscoveryById(id);
		return Response.ok("Discovery was removed").build();

    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getALL(@QueryParam("orderBy") @DefaultValue("popular") String order,@QueryParam("beginWith") Integer begin,
						   @QueryParam("endWith") Integer end) {
		List<Discovery> allDiscoveries;
	if(begin!=null && !(begin<0) && end!=null && !(end<0)){
		allDiscoveries=discoveryService.getWithLimit(begin,end);
		}
	else {
		allDiscoveries = discoveryService.getAll(order);
	}
	if(allDiscoveries.isEmpty()){
		return Response.status(Response.Status.NO_CONTENT).build();
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
