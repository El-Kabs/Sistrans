package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Ingrediente;
import vos.Producto;
import vos.Usuario;
import vos.VOVerificacionIngrediente;
import vos.VOVerificacionProducto;

@Path("ingredientes")
public class RotondAndesServicesIngrediente {
	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	

	/**
	 * Metodo que expone servicio REST usando GET que da todos los videos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIngredientes() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Ingrediente> ingredientes;
		try {
			ingredientes = tm.darIngredientes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingredientes).build();
	}

    /**
     * Metodo que expone servicio REST usando GET que busca el video con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre del video a buscar que entra en la URL como parametro 
     * @return Json con el/los videos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombre}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getIngredienteName( @QueryParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Ingrediente> ingredientes;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del producto no valido");
			ingredientes = tm.buscarIngredientesPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingredientes).build();
	}

	@POST
	@Path("restaurante")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addIngredienteByRestaurante(VOVerificacionIngrediente verificacion)
	{
		Usuario restaurante = verificacion.getRestaurante();
		Ingrediente usu = verificacion.getIngrediente();
		try {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			if(verificarcontraseņa(restaurante)&&restaurante.getRol().equals("RESTAURANTE"))
			{
				System.out.println("entro IF");
				tm.addIngrediente(usu);
				return Response.status(200).entity(usu).build();
			}
			System.out.println("no entro");
			return Response.status(402).entity("no puede registrar este usuario").build();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
	}
	

    /**
     * Metodo que expone servicio REST usando POST que agrega el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
     * @param usuario - video a agregar
     * @return Json con el video que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addIngrediente(Ingrediente ingrediente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addIngrediente(ingrediente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingrediente).build();
	}
	
    /**
     * Metodo que expone servicio REST usando POST que agrega los videos que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/varios
     * @param usuarios - videos a agregar. 
     * @return Json con el video que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addIngredientes(List<Ingrediente> ingrediente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addIngredientes(ingrediente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingrediente).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
     * @param usuario - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateIngrediente(Ingrediente ingrediente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateIngrediente(ingrediente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingrediente).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
     * @param usuario - video a aliminar. 
     * @return Json con el video que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProducto(Ingrediente ingrediente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteIngrediente(ingrediente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingrediente).build();
	}
	
	public boolean verificarcontraseņa(Usuario usuario)
	{
		boolean correcta=false;
		try {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario usu= tm.buscarUsuarioPorId(usuario.getId());
		System.out.println(usu.getPassword()+"   base datos");
		System.out.println(usuario.getPassword());
		System.out.println("VERIFICANDO PASS");
		if(usu.getPassword().equals(usuario.getPassword())) {
			System.out.println("verificado");
			correcta=true;
		}
		
		}
		catch(Exception e)
		{
			
		}
		return correcta;
	}
}
