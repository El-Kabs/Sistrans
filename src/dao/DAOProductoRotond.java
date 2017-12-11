package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Categoria;
import vos.Producto;
import vos.Usuario;

public class DAOProductoRotond {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOProductoRotond() {
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
	public ArrayList<Producto> darProductos() throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		System.out.println("Casi SQL");
		String sql = "SELECT * FROM PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		System.out.println("Creo que aca vale verga");
		int count = 0;
		while (rs.next() && count<=2) {
			String nombre = rs.getString("NOMBRE");
			String info = rs.getString("INFORMACION");
			String traduccion = rs.getString("TRADUCCION");
			String preparacion = rs.getString("PREPARACION");
			double costo = rs.getDouble("COSTO_PRODUCCION");
			double precio = rs.getDouble("PRECIO");
			Categoria categoria = Categoria.valueOf(rs.getString("CATEGORIA"));
			productos.add(new Producto(nombre, info, preparacion, traduccion, costo, precio, categoria));
			count++;
		}
		return productos;
	}


	/**
	 * Metodo que busca el/los videos con el nombre que entra como parametro.
	 * @param name - Nombre de el/los videos a buscar
	 * @return ArrayList con los videos encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Producto> buscarProductoPorName(String name) throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTO WHERE NOMBRE ='" + name + "'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String info = rs.getString("INFORMACION");
			String traduccion = rs.getString("TRADUCCION");
			String preparacion = rs.getString("PREPARACION");
			double costo = rs.getDouble("COSTO_PRODUCCION");
			double precio = rs.getDouble("PRECIO");
			Categoria categoria = Categoria.valueOf(rs.getString("CATEGORIA"));
			productos.add(new Producto(nombre, info, preparacion, traduccion, costo, precio, categoria));
		}
		return productos;
	}

	/**
	 * Metodo que agrega el video que entra como parametro a la base de datos.
	 * @param usuario - el video a agregar. video !=  null
	 * <b> post: </b> se ha agregado el video a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que el video baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addProducto(Producto producto) throws SQLException, Exception {
		
		String sql2 = "INSERT INTO PRODUCTO VALUES ('"+producto.getNombre()+"', '"+producto.getInfo()+"', '"+producto.getTraduccion()+"', '"+producto.getPreparacion()+"', "+producto.getCostoProduccion()+","+producto.getPrecio()+",'"+producto.getCategoria()+"')";
//		INSERT INTO USUARIOS VALUES(1, 'Kobs', 'kobs@kobs.com', 'admin', 'kabska83');

		PreparedStatement prepStmt = conn.prepareStatement(sql2);
		System.out.println("SQL 2:"+sql2);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Metodo que actualiza el video que entra como parametro en la base de datos.
	 * @param usuario - el video a actualizar. video !=  null
	 * <b> post: </b> se ha actualizado el video en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateProducto(Producto producto) throws SQLException, Exception {

		String sql2 = "UPDATE PRODUCTO SET INFORMACION = '"+producto.getInfo()+"', TRADUCCION='"+producto.getTraduccion()+"', PREPARACION='"+producto.getPreparacion()+"', COSTO_PRODUCCION="+producto.getCostoProduccion()+", PRECIO="+producto.getPrecio()+", CATEGORIA='"+producto.getCategoria()+"' WHERE NOMBRE="+producto.getNombre();
		
		PreparedStatement prepStmt = conn.prepareStatement(sql2);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina el video que entra como parametro en la base de datos.
	 * @param usuario - el video a borrar. video !=  null
	 * <b> post: </b> se ha borrado el video en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteProducto(Producto producto) throws SQLException, Exception {

		String sql = "DELETE FROM PRODUCTO";
		sql += " WHERE NAME= " + producto.getNombre();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
