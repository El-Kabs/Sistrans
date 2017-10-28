package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Categoria;
import vos.Ingrediente;
import vos.Producto;
import vos.ProductoIngrediente;
import vos.Restaurante;
import vos.RestauranteProducto;

public class DAOProductoIngrediente {

	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOProductoIngrediente() {
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
	public ArrayList<ProductoIngrediente> darProductoIngrediente() throws SQLException, Exception {
		ArrayList<ProductoIngrediente> productoIngrediente = new ArrayList<ProductoIngrediente>();
		String sql = "SELECT * FROM(SELECT * FROM PRODUCTO a JOIN PRODUCTO_INGREDIENTES c ON a.NOMBRE=c.NOMBRE_PRODUCTO)e JOIN INGREDIENTE f ON e.NOMBRE_INGREDIENTE=f.NOMBRE"; 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			String informacionProducto = rs.getString("INFORMACION");
			String traduccionProducto = rs.getString("TRADUCCION");
			String preparacionProducto = rs.getString("PREPARACION");
			int costoProducto = rs.getInt("COSTO_PRODUCCION");
			double precioProducto = rs.getDouble("PRECIO");
			Categoria categoriaProducto = Categoria.valueOf(rs.getString("CATEGORIA"));
			String nombreIngrediente = rs.getString("NOMBRE_INGREDIENTE");
			String descripcionIngrediente = rs.getString("DESCRIPCION");
			String traduccionIngrediente = rs.getString(12);
			Producto producto = new Producto(nombreProducto, informacionProducto, traduccionProducto, preparacionProducto, costoProducto, precioProducto, categoriaProducto);
			Ingrediente ingrediente = new Ingrediente(nombreIngrediente, descripcionIngrediente, traduccionIngrediente);
			productoIngrediente.add(new ProductoIngrediente(producto, ingrediente));
		}
		return productoIngrediente;
	}

	public void addProductoIngrediente(ProductoIngrediente ingredienteProducto) throws SQLException, Exception {
			String sql2 = "INSERT INTO PRODUCTO_INGREDIENTES VALUES ('"+ingredienteProducto.getProducto().getNombre()+"', '"+ingredienteProducto.getIngrediente().getNombre()+"')";
			System.out.println(sql2);
			PreparedStatement prepStmt = conn.prepareStatement(sql2);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
	}
	
	public ProductoIngrediente buscarIngredienteProductoPorNameProducto(String name) throws SQLException, Exception 
	{
		ProductoIngrediente prodIngre = null;

		String sql ="SELECT * FROM(SELECT * FROM PRODUCTO a JOIN PRODUCTO_INGREDIENTES c ON a.NOMBRE=c.NOMBRE_PRODUCTO)e JOIN INGREDIENTE f ON e.NOMBRE_INGREDIENTE=f.NOMBRE WHERE e.NOMBRE = '" + name+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			int cantidadProducto = rs.getInt("CANTIDAD");
			String informacionProducto = rs.getString("INFORMACION");
			String traduccionProducto = rs.getString("TRADUCCION");
			String preparacionProducto = rs.getString("PREPARACION");
			int costoProducto = rs.getInt("COSTO_PRODUCCION");
			double precioProducto = rs.getDouble("PRECIO");
			int max = rs.getInt("MAX");
			Categoria categoriaProducto = Categoria.valueOf(rs.getString("CATEGORIA"));
			String nombreIngrediente = rs.getString("NOMBRE_INGREDIENTE");
			String descripcionIngrediente = rs.getString("DESCRIPCION");
			String traduccionIngrediente = rs.getString(12);
			Producto producto = new Producto(nombreProducto, informacionProducto, traduccionProducto, preparacionProducto, costoProducto, precioProducto, categoriaProducto);
			Ingrediente ingrediente = new Ingrediente(nombreIngrediente, descripcionIngrediente, traduccionIngrediente);
			prodIngre = new ProductoIngrediente(producto, ingrediente);
		}
		return prodIngre;
	}
	
	public ProductoIngrediente buscarIngredienteProductoPorNameIngrediente(String name) throws SQLException, Exception 
	{
		ProductoIngrediente prodIngre = null;
		
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();

		String sql ="SELECT * FROM(SELECT * FROM PRODUCTO a JOIN PRODUCTO_INGREDIENTES c ON a.NOMBRE=c.NOMBRE_PRODUCTO)e JOIN INGREDIENTE f ON e.NOMBRE_INGREDIENTE=f.NOMBRE WHERE e.NOMBRE = '" + name+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			int cantidadProducto = rs.getInt("CANTIDAD");
			String informacionProducto = rs.getString("INFORMACION");
			String traduccionProducto = rs.getString("TRADUCCION");
			String preparacionProducto = rs.getString("PREPARACION");
			int costoProducto = rs.getInt("COSTO_PRODUCCION");
			double precioProducto = rs.getDouble("PRECIO");
			int max = rs.getInt("MAX");
			Categoria categoriaProducto = Categoria.valueOf(rs.getString("CATEGORIA"));
			String nombreIngrediente = rs.getString("NOMBRE_INGREDIENTE");
			String descripcionIngrediente = rs.getString("DESCRIPCION");
			String traduccionIngrediente = rs.getString(12);
			Producto producto = new Producto(nombreProducto, informacionProducto, traduccionProducto, preparacionProducto, costoProducto, precioProducto, categoriaProducto);
			Ingrediente ingrediente = new Ingrediente(nombreIngrediente, descripcionIngrediente, traduccionIngrediente);
			prodIngre = new ProductoIngrediente(producto, ingrediente);
		}
		return prodIngre;
	}
}
