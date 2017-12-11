/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogot√° - Colombia)
 * Departamento de Ingenier√≠a de Sistemas y Computaci√≥n
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe Garc√≠a - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
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

import org.codehaus.jackson.map.ObjectMapper;

import tm.RotondAndesTM;
import vos.Restaurante;
import vos.RestauranteProducto;
import vos.Usuario;
import vos.VOReabastecimiento;
import vos.VOVerificacionCliente;
import vos.VOVerificacionRestaurante;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/VideoAndes/rest/videos/...
 * @author Monitores 2017-20
 */
@Path("Restaurantes")
public class RotondAndesServicesRestaurante {

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
	public Response getRestaurantes() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Restaurante> Restaurantes;
		try {
			Restaurantes = tm.darRestaurantes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Restaurantes).build();
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
	public Response getRestauranteName( @QueryParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Restaurante> Restaurantes;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del Restaurante no valido");
			Restaurantes = tm.buscarRestaurantePorNombre(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Restaurantes).build();
	}
	
	@GET
	@Path( "consulta/{nombre}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getRestauranteConsulta( @QueryParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		String retorno = "";
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del Restaurante no valido");
			retorno = tm.consultarRestauranteRentabilidad(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(retorno).build();
	}
	

    /**
     * Metodo que expone servicio REST usando POST que agrega el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
     * @param Restaurante - video a agregar
     * @return Json con el video que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurante(VOVerificacionRestaurante verificacion) {
		Usuario admin=verificacion.getAdmin();
		Restaurante rest=verificacion.getRestaurante();
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			if(verificarcontraseÒa(admin)&&admin.getRol().equals("Administrador"))
			{
				tm.addRestaurante(rest);
				return Response.status(200).entity(rest).build();

			}
			return Response.status(402).entity("no se pudo agregar el restaurante").build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
    /**
     * Metodo que expone servicio REST usando POST que agrega los videos que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/varios
     * @param Restaurantes - videos a agregar. 
     * @return Json con el video que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurantes(List<Restaurante> Restaurantes) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addRestaurantes(Restaurantes);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Restaurantes).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
     * @param Restaurante - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRestaurante(Restaurante Restaurante) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateRestaurante(Restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Restaurante).build();
	}
	
	   /**
     * Metodo que expone servicio REST usando PUT que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
     * @param Restaurante - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@PUT
	@Path( "reabastecer" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reabastecerRestaurante(VOReabastecimiento reabastecimiento) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			if(verificarcontraseÒa(reabastecimiento.getRestaurante())&&reabastecimiento.getRestaurante().getRol().equals("RESTAURANTE"))
			{
				tm.reabastecerRestaurante(reabastecimiento.getRestauranteProducto());
				return Response.status(200).entity(reabastecimiento.getRestauranteProducto()).build();
			}
			return Response.status(402).entity(doErrorMessage(new Exception("no se pudo reabastecer el restaurante"))).build();
		} catch (Exception e) {
			System.out.println("me putee");
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
     * @param Restaurante - video a aliminar. 
     * @return Json con el video que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRestaurante(Restaurante Restaurante) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteRestaurante(Restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Restaurante).build();
	}
	
	public boolean verificarcontraseÒa(Usuario usuario)
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
