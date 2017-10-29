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
import vos.Usuario;
import vos.VOConsultaUsuarioPedidos;
import vos.VOUsuarioConsulta;
import vos.VOVerificacionCliente;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/VideoAndes/rest/videos/...
 * @author Monitores 2017-20
 */
@Path("usuarios")
public class RotondAndesServicesUsuario {

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
	public Response getUsuarios() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Usuario> usuarios;
		try {
			usuarios = tm.darUsuarios();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
	}
	
	/**
	 * Metodo que expone servicio REST usando GET que da todos los videos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path("detailed")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClientes() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<VOUsuarioConsulta> usuarios;
		try {
			usuarios = tm.darClientes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
	}
	
	

	 /**
     * Metodo que expone servicio REST usando GET que busca el video con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/<<id>>" para la busqueda"
     * @param name - Nombre del video a buscar que entra en la URL como parametro 
     * @return Json con el/los videos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getUsuario( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Usuario v = tm.buscarUsuarioPorId( id );
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
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
		List<Usuario> usuarios;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del usuario no valido");
			usuarios = tm.buscarUsuariosPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
	}

	@POST
	@Path("admin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUserByAdmin(VOVerificacionCliente verificacion)
	{
		Usuario admin=verificacion.getAdmin();
		Usuario usu=verificacion.getUsuario();
		try {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			System.out.println("entro al Try");
			System.out.println(admin.getNombre());
			System.out.println(usu.getNombre());
			if(verificarcontrase�a(admin)&&admin.getRol().equals("Administrador"))
			{
				System.out.println("entro IF");
				tm.addUsuario(usu);
				return Response.status(200).entity(usu).build();
			}
			System.out.println("no entro");
			return Response.status(402).entity(doErrorMessage(new Exception("no puede agregar el usuario"))).build();
			
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
	public Response addUsuario(Usuario usuario) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addUsuario(usuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuario).build();
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
	public Response addUsuarios(List<Usuario> usuarios) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addUsuarios(usuarios);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
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
	public Response updateUsuario(Usuario usuario) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateUsuario(usuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuario).build();
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
	public Response deleteUsuario(Usuario usuario) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteUsuario(usuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuario).build();
	}
	
	@GET
	@Path("{id}/consultarPedidos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarPedidos(@PathParam("id") Long id)
	{
		RotondAndesTM tm= new RotondAndesTM(getPath());
		VOConsultaUsuarioPedidos resp;
		try {
			resp=tm.consultarPedidos(id);
		}
		catch(Exception e)
		{
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(resp).build();
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
