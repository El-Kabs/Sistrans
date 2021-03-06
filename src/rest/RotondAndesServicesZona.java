/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
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
import vos.Zona;
import vos.Usuario;
import vos.VOConsultaZona;
import vos.VOVerificacionCliente;
import vos.VOVerificacionIngrediente;
import vos.VOVerificacionZona;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/VideoAndes/rest/videos/...
 * @author Monitores 2017-20
 */
@Path("Zonas")
public class RotondAndesServicesZona {

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
	public Response getZonas() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Zona> Zonas;
		try {
			Zonas = tm.darZonas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Zonas).build();
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
	public Response getZonaName( @PathParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<VOConsultaZona> Zonas;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del Zona no valido");
			Zonas = tm.darZonaInfoPorNombre(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Zonas).build();
	}

	@POST
	@Path("admin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addZonaByAdmin(VOVerificacionZona verificacion)
	{
		Usuario admin=verificacion.getAdmin();
		Zona zona=verificacion.getZona();
		try {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			System.out.println("entro al Try");
			System.out.println(admin.getNombre());
			System.out.println(zona.getNombre());
			if(verificarcontrase�a(admin)&&admin.getRol().equals("Administrador"))
			{
				System.out.println("entro IF");
				tm.addZona(zona);
				return Response.status(200).entity(zona).build();
			}
			System.out.println("no entro");
			return Response.status(402).entity("no puede registrar este Zona").build();
			
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
     * @param Zona - video a agregar
     * @return Json con el video que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addZona(Zona Zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addZona(Zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Zona).build();
	}
	
    /**
     * Metodo que expone servicio REST usando POST que agrega los videos que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/varios
     * @param Zonas - videos a agregar. 
     * @return Json con el video que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addZonas(List<Zona> Zonas) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addZonas(Zonas);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Zonas).build();
	}
	
//    /**
//     * Metodo que expone servicio REST usando PUT que actualiza el video que recibe en Json
//     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
//     * @param Zona - video a actualizar. 
//     * @return Json con el video que actualizo o Json con el error que se produjo
//     */
//	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response updateZona(Zona Zona) {
//		RotondAndesTM tm = new RotondAndesTM(getPath());
//		try {
//			tm.updateZona(Zona);
//		} catch (Exception e) {
//			return Response.status(500).entity(doErrorMessage(e)).build();
//		}
//		return Response.status(200).entity(Zona).build();
//	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
     * @param Zona - video a aliminar. 
     * @return Json con el video que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteZona(Zona Zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteZona(Zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(Zona).build();
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
