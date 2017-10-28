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
import vos.VOEquivalenciaProducto;

@Path("equivProd")
public class RotondAndesServicesEquivProductos {

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
		List<VOEquivalenciaProducto> equivalencias;
		try {
			equivalencias = tm.darEquivalenciaProd();
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
		VOEquivalenciaProducto equivalencias;
		try {
			if (id == null)
				throw new Exception("ID De la equivalencia no valido");
			equivalencias = tm.buscarEquivProdPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(equivalencias).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEquivalencia(VOEquivalenciaProducto equiv) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Producto prod1 = tm.buscarProductosPorName(equiv.getProducto1().getNombre()).get(0);
			Producto prod2 = tm.buscarProductosPorName(equiv.getProducto2().getNombre()).get(0);
			equiv.setProducto1(prod1);
			equiv.setProducto2(prod2);
			tm.addEquivalencia(equiv);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(equiv).build();
	}
}
