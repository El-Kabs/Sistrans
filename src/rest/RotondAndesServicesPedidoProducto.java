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
import vos.Ingrediente;
import vos.Menu;
import vos.PedidoMenu;
import vos.PedidoMesa;
import vos.PedidoProducto;
import vos.RestauranteProducto;
import vos.Usuario;
import vos.VORestaurantePedidoProducto;
import vos.VOVerificacionMenu;

@Path("pedidoProducto")
public class RotondAndesServicesPedidoProducto {
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

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPedidosProductos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<PedidoProducto> pedidosProductos;
		try {
			pedidosProductos = tm.darPedidoProductos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidosProductos).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPedido(PedidoProducto pedidoProducto)
	{
		try {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			tm.addPedidoProducto(pedidoProducto);
			return Response.status(200).entity(pedidoProducto).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	

	@POST
	@Path("menu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPedidoMenu(PedidoMenu pedidoMenu)
	{
		try {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			tm.addPedidoMenu(pedidoMenu);
			return Response.status(200).entity(pedidoMenu).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	@POST
	@Path("mesa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response addPedidoMesa(PedidoMesa pedido)
	{
		try {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			tm.addPedidoMesa(pedido);
			return Response.status(200).entity(pedido).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	@Path( "{nombre}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPedidoProductoName( @QueryParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		PedidoProducto pedidoProducto;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del producto no valido");
			pedidoProducto = tm.buscarPedidoProductoPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidoProducto).build();
	}


	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPedidoProducto( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			PedidoProducto v = tm.buscarPedidoProductoPorID(id);
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}

	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces( { MediaType.APPLICATION_JSON } )
	public void updatePedidoEstado( VORestaurantePedidoProducto algoquenecesito)
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			 tm.updatePedidoEstado(algoquenecesito.getPedido(), algoquenecesito.getRestaurante());	
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}
	@PUT
	@Path("menu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces( { MediaType.APPLICATION_JSON } )
	public void updatePedidoMenuEstado( PedidoMenu pedidoMenu,VORestaurantePedidoProducto algoquenecesito) //Aca hay que poner el MEGAJSON
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			 tm.updatePedidoMenuEstado(pedidoMenu, algoquenecesito.getRestaurante());	
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}
	@PUT
	@Path("mesa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces( { MediaType.APPLICATION_JSON } )
	public void updatePedidoEstado(PedidoMesa pedido, VORestaurantePedidoProducto algoquenecesito) // Aca Tambien
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			 tm.updatePedidoEstado(algoquenecesito.getPedido(), algoquenecesito.getRestaurante());	
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}
	
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelarPedidoProducto(PedidoProducto pedidoProducto)
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			tm.deletePedidoProducto(pedidoProducto);
			 return Response.status( 200 ).entity( doErrorMessage(new Exception("EXITO")) ).build( );			
		}
		catch( Exception e )
		{
			 return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	@DELETE
	@Path("menu")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelarPedidoProducto(PedidoMenu pedidoMenu)
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			tm.deletePedidoMenu(pedidoMenu);
			 return Response.status( 200 ).entity(doErrorMessage(new Exception("EXITO")) ).build( );			
		}
		catch( Exception e )
		{
			 return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	@DELETE
	@Path("mesa")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelarPedidoMesa(PedidoMesa pedidoMesa)
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			tm.deletePedidoMesa(pedidoMesa);
			 return Response.status( 200 ).entity(doErrorMessage(new Exception("EXITO")) ).build( );			
		}
		catch( Exception e )
		{
			 return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}
