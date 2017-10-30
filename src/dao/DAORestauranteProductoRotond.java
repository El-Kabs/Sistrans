package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Categoria;
import vos.Pedido;
import vos.PedidoProducto;
import vos.Producto;
import vos.Restaurante;
import vos.RestauranteProducto;

public class DAORestauranteProductoRotond {
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAORestauranteProductoRotond() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan enel arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexión que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}

	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los videos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM VIDEOS;
	 * @return Arraylist con los videos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<RestauranteProducto> darRestauranteProducto() throws SQLException, Exception {
		ArrayList<RestauranteProducto> restauranteProducto = new ArrayList<RestauranteProducto>();
		String sql = "SELECT * FROM(SELECT * FROM RESTAURANTE a JOIN RESTAURANTE_PRODUCTO c ON a.NOMBRE=c.NOMBRE_RESTAURANTE)e JOIN PRODUCTO f ON e.NOMBRE_PRODUCTO=f.NOMBRE"; 
		ArrayList<Producto> productos = new ArrayList<Producto>();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreRestaurante = rs.getString("NOMBRE");
			String representanteRestaurante = rs.getString("REPRESENTANTE");
			String paginaRestaurante = rs.getString("PAGINA_WEB");
			String tipoRestaurante = rs.getString("TIPO_COMIDA_RESTAURANTE");
			String zonaRestaurante = rs.getString("ZONA_RESTAURANTE");
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			int cantidadProducto = rs.getInt("CANTIDAD");
			String informacionProducto = rs.getString("INFORMACION");
			String traduccionProducto = rs.getString("TRADUCCION");
			String preparacionProducto = rs.getString("PREPARACION");
			int costoProducto = rs.getInt("COSTO_PRODUCCION");
			double precioProducto = rs.getDouble("PRECIO");
			int max = rs.getInt("MAX");
			Categoria categoriaProducto = Categoria.valueOf(rs.getString("CATEGORIA"));
			Producto producto = new Producto(nombreProducto, informacionProducto, traduccionProducto, preparacionProducto, costoProducto, precioProducto, categoriaProducto);
			Restaurante restaurante = new Restaurante(nombreRestaurante, representanteRestaurante, paginaRestaurante, tipoRestaurante, zonaRestaurante);
			restauranteProducto.add(new RestauranteProducto(restaurante, producto, cantidadProducto, max));
		}
		return restauranteProducto;
	}

	public void addRestauranteProducto(RestauranteProducto restauranteProducto	) throws SQLException, Exception {
			String sql2 = "INSERT INTO RESTAURANTE_PRODUCTO VALUES ('"+restauranteProducto.getRestaurante().getNombre()+"', '"+restauranteProducto.getProducto().getNombre()+"', "+restauranteProducto.getCantidad()+", "+restauranteProducto.getMax()+")";
			System.out.println(sql2);
			PreparedStatement prepStmt = conn.prepareStatement(sql2);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
	}
	
	public RestauranteProducto buscarRestauranteProductoPorNameRestaurante(String name) throws SQLException, Exception 
	{
		RestauranteProducto restauranteProducto = null;
		
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();

		String sql = "SELECT * FROM(SELECT * FROM RESTAURANTE a JOIN RESTAURANTE_PRODUCTO c ON a.NOMBRE=c.NOMBRE_RESTAURANTE)e JOIN PRODUCTO f ON e.NOMBRE_PRODUCTO=f.NOMBRE' WHERE e.NOMBRE = '" + name+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreRestaurante = rs.getString("NOMBRE");
			String representanteRestaurante = rs.getString("REPRESENTANTE");
			String paginaRestaurante = rs.getString("PAGINA_WEB");
			String tipoRestaurante = rs.getString("TIPO_COMIDA_RESTAURANTE");
			String zonaRestaurante = rs.getString("ZONA_RESTAURANTE");
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			int cantidadProducto = rs.getInt("CANTIDAD");
			String informacionProducto = rs.getString("INFORMACION");
			String traduccionProducto = rs.getString("TRADUCCION");
			String preparacionProducto = rs.getString("PREPARACION");
			int costoProducto = rs.getInt("COSTO_PRODUCCION");
			double precioProducto = rs.getDouble("PRECIO");
			int max = rs.getInt("MAX");
			Categoria categoriaProducto = Categoria.valueOf(rs.getString("CATEGORIA"));
			Producto producto = new Producto(nombreProducto, informacionProducto, traduccionProducto, preparacionProducto, costoProducto, precioProducto, categoriaProducto);
			Restaurante restaurante = new Restaurante(nombreRestaurante, representanteRestaurante, paginaRestaurante, tipoRestaurante, zonaRestaurante);
			restauranteProducto = new RestauranteProducto(restaurante, producto, cantidadProducto, max);
		}
		return restauranteProducto;
	}
	
	public RestauranteProducto buscarRestauranteProductoPorNameProducto(String name) throws SQLException, Exception 
	{
		RestauranteProducto restauranteProducto = null;
		
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();

		String sql = "SELECT * FROM(SELECT * FROM RESTAURANTE a JOIN RESTAURANTE_PRODUCTO c ON a.NOMBRE=c.NOMBRE_RESTAURANTE)e JOIN PRODUCTO f ON e.NOMBRE_PRODUCTO=f.NOMBRE WHERE e.Nombre_Producto = '" + name+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreRestaurante = rs.getString("NOMBRE");
			String representanteRestaurante = rs.getString("REPRESENTANTE");
			String paginaRestaurante = rs.getString("PAGINA_WEB");
			String tipoRestaurante = rs.getString("TIPO_COMIDA_RESTAURANTE");
			String zonaRestaurante = rs.getString("ZONA_RESTAURANTE");
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			int cantidadProducto = rs.getInt("CANTIDAD");
			String informacionProducto = rs.getString("INFORMACION");
			String traduccionProducto = rs.getString("TRADUCCION");
			String preparacionProducto = rs.getString("PREPARACION");
			int costoProducto = rs.getInt("COSTO_PRODUCCION");
			double precioProducto = rs.getDouble("PRECIO");
			int max = rs.getInt("MAX");
			Categoria categoriaProducto = Categoria.valueOf(rs.getString("CATEGORIA"));
			Producto producto = new Producto(nombreProducto, informacionProducto, traduccionProducto, preparacionProducto, costoProducto, precioProducto, categoriaProducto);
			Restaurante restaurante = new Restaurante(nombreRestaurante, representanteRestaurante, paginaRestaurante, tipoRestaurante, zonaRestaurante);
			restauranteProducto = new RestauranteProducto(restaurante, producto, cantidadProducto, max);
		}
		return restauranteProducto;
	}
	
	public void disminuirCantidad(RestauranteProducto restaurante){
		String sql = "UPDATE RESTAURANTE_PRODUCTO SET CANTIDAD = CANTIDAD - 1 WHERE NOMBRE_PRODUCTO='"+restaurante.getProducto().getNombre()+"'";
	}
	public void reabastecer(RestauranteProducto restaurante) throws SQLException
	{
		String sql ="UPDATE RESTAURANTE_PRODUCTO SET CANTIDAD=MAX WHERE NOMBRE_PRODUCTO='"+restaurante.getProducto().getNombre()+"' AND NOMBRE_RESTAURANTE='"+restaurante.getRestaurante().getNombre()+"' ";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void actualizarCantidad(RestauranteProducto restaurante) throws SQLException
	{
		String sql ="UPDATE RESTAURANTE_PRODUCTO SET CANTIDAD=CANTIDAD-1 WHERE NOMBRE_PRODUCTO='"+restaurante.getProducto().getNombre()+"' AND NOMBRE_RESTAURANTE='"+restaurante.getRestaurante().getNombre()+"' ";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
}
