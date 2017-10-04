package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Menu;
import vos.Usuario;
import vos.VOVerificacionCliente;
import vos.VOVerificacionMenu;

@Path("Menus")
public class RotondAndesServicesMenu {
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
	public Response getMenus() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Menu> Menus;
		try {
			Menus = tm.darMenus();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Menus).build();
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
	public Response getUsuarioName( @QueryParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Menu> Menus;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del Menu no valido");
			Menus = tm.buscarMenuPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Menus).build();
	}

	@POST
	@Path("restaurante")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMenuByRestaurante(VOVerificacionMenu verificacion)
	{
		Usuario restaurante = verificacion.getRestaurante();
		Menu usu = verificacion.getMenu();
		try {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			if(verificarcontrase�a(restaurante)&&restaurante.getRol().equals("RESTAURANTE"))
			{
				System.out.println("entro IF");
				tm.addMenu(usu);
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
	public Response addMenu(Menu Menu) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addMenu(Menu);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Menu).build();
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
	public Response addMenu(List<Menu> Menus) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addMenus(Menus);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Menus).build();
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
	public Response updateMenu(Menu Menu) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateMenu(Menu);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Menu).build();
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
	public Response deleteMenu(Menu Menu) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteMenu(Menu);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Menu).build();
	}
	
	public boolean verificarcontrase�a(Usuario usuario)
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
