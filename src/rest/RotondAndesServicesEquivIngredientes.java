package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Ingrediente;
import vos.Producto;
import vos.VOEquivalenciaIngrediente;
import vos.VOEquivalenciaProducto;

@Path("equivIngre")
public class RotondAndesServicesEquivIngredientes {
	@Context
	private ServletContext context;
	
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getEquivalencias() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<VOEquivalenciaIngrediente> equivalencias;
		try {
			equivalencias = tm.darEquivalenciaIngre();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(equivalencias).build();
	}
	
	@GET
	@Path( "{id}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getIngredienteName( @QueryParam("id") Integer id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		VOEquivalenciaIngrediente equivalencias;
		try {
			if (id == null)
				throw new Exception("ID De la equivalencia no valido");
			equivalencias = tm.buscarEquivIngrePorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(equivalencias).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEquivalencia(VOEquivalenciaIngrediente equiv) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Ingrediente prod1 = tm.buscarIngredientesPorName(equiv.getIngrediente1().getNombre()).get(0);
			Ingrediente prod2 = tm.buscarIngredientesPorName(equiv.getIngrediente2().getNombre()).get(0);
			equiv.setIngrediente1(prod1);
			equiv.setIngrediente2(prod2);
			tm.addEquivalenciaIngre(equiv);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(equiv).build();
	}
}
