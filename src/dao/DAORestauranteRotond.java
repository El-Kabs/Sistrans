package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Ingrediente;
import vos.Restaurante;

public class DAORestauranteRotond {
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAORestauranteRotond() {
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
	public ArrayList<Restaurante> darRestaurante() throws SQLException, Exception {
		ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();

		String sql = "SELECT * FROM RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String representante = rs.getString("REPRESENTANTE");
			String pagina = rs.getString("PAGINA_WEB");
			String tipo = rs.getString("TIPO_COMIDA_RESTAURANTE");
			String zona = rs.getString("ZONA_RESTAURANTE");
			restaurantes.add(new Restaurante(nombre, representante, pagina, tipo, zona));
		}
		return restaurantes;
	}
	
	public ArrayList<Restaurante> buscarRestaurantesPorName(String name) throws SQLException, Exception {
		ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();

		String sql = "SELECT * FROM RESTAURANTE WHERE NOMBRE ='" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String representante = rs.getString("REPRESENTANTE");
			String pagina = rs.getString("PAGINA_WEB");
			String tipo = rs.getString("TIPO_COMIDA_RESTAURANTE");
			String zona = rs.getString("ZONA_RESTAURANTE");
			restaurantes.add(new Restaurante(nombre, representante, pagina, tipo, zona));
		}
		return restaurantes;
	}
	
	public String rentabilidadRestaurante(String name) throws SQLException, Exception {

		String sql = "SELECT * FROM (SELECT * FROM RESTAURANTE_PRODUCTO JOIN (SELECT * FROM PEDIDO A JOIN PEDIDO_PRODUCTO B ON A.ID = B.ID_PEDIDO) A ON RESTAURANTE_PRODUCTO.NOMBRE_PRODUCTO = A.NOMBRE_PRODUCTO) B WHERE NOMBRE_RESTAURANTE = '"+name+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		int numProductos = 0;
		int costoTotal = 0;
		
		while (rs.next()) {
			numProductos++;
			costoTotal += rs.getInt("COSTO_TOTAL");
		}
		return "El numero de productos vendidos fue: "+numProductos+" y se facturo: "+costoTotal;
	}

	/**
	 * Metodo que agrega el video que entra como parametro a la base de datos.
	 * @param usuario - el video a agregar. video !=  null
	 * <b> post: </b> se ha agregado el video a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que el video baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addRestaurante(Restaurante restaurante) throws SQLException, Exception {
		
		String sql2 = "INSERT INTO RESTAURANTE VALUES ('"+restaurante.getNombre()+"', '"+restaurante.getRepresentante()+"', '"+restaurante.getPagina()+"', '"+restaurante.getTipo()+"', '"+restaurante.getZona()+"')";

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
	public void updateRestaurante(Restaurante restaurante) throws SQLException, Exception {

		String sql2 = "UPDATE RESTAURANTE SET REPRESENTANTE='"+restaurante.getRepresentante()+"', PAGINA_WEB='"+restaurante.getPagina()+" TIPO_COMIDA_RESTAURANTE = ' "+restaurante.getTipo()+"' ZONA_RESTAURANTE='"+restaurante.getZona()+"' WHERE NAME='"+restaurante.getNombre()+"'"
		;
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
	public void deleteRestaurante(Restaurante restaurante) throws SQLException, Exception {

		String sql = "DELETE FROM RESTAURANTE";
		sql += " WHERE NAME = " + restaurante.getNombre();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
