package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.ProductoIngrediente;
import vos.RestauranteProducto;

@Path("ProductoIngrediente")
public class RotondAndesServicesProdIngre {
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
	public Response getIngredientesPorProducto() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ProductoIngrediente> restprod;
		try {
			restprod = tm.darProdIngre();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restprod).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestauranteProducto(ProductoIngrediente equiv) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addProductoIngrediente(equiv);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(equiv).build();
	}
}
