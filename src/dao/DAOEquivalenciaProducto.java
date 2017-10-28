package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Categoria;
import vos.Producto;
import vos.Usuario;
import vos.VOEquivalenciaProducto;

public class DAOEquivalenciaProducto {
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
	public DAOEquivalenciaProducto() {
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
	
	public ArrayList<VOEquivalenciaProducto> darEquivalenciaProd() throws SQLException, Exception {
		ArrayList<VOEquivalenciaProducto> productos = new ArrayList<VOEquivalenciaProducto>();

		String sql = "SELECT * FROM EQUIVALENCIA_PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String producto1 = rs.getString("NOMBRE_PRODUCTO_1");
			String producto2 = rs.getString("NOMBRE_PRODUCTO_2");
			Long id = rs.getLong("ID");
			Producto prod1 = darProducto(producto1);
			Producto prod2 = darProducto(producto2);
			VOEquivalenciaProducto vo = new VOEquivalenciaProducto(prod1, prod2, id);
			productos.add(vo);
		}
		return productos;
	}
	
	public Producto darProducto(String nombreP) throws SQLException, Exception {
		String sql = "SELECT * FROM PRODUCTO WHERE NOMBRE = '"+nombreP+"'";
		Producto retorno = null;
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
			retorno = new Producto(nombre, info, preparacion, traduccion, costo, precio, categoria);
		}
		return retorno;
	}
	
	public boolean esEquivalenteProducto(Producto p1, Producto p2) throws SQLException, Exception {
		String sql = "SELECT * FROM EQUIVALENCIA_PRODUCTO WHERE NOMBRE_PRODUCTO_1 = '"+p1.getNombre()+"' AND NOMBRE_PRODUCTO_2='"+p2.getNombre()+"' OR NOMBRE_PRODUCTO_1 = '"+p2.getNombre()+"' AND NOMBRE_PRODUCTO_2='"+p1.getNombre()+"'";
		String n1 = "";
		String n2 = "";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE_PRODUCTO_1");
			String nombre2 = rs.getString("NOMBRE_PRODUCTO_2");
			n1 = nombre;
			n2 = nombre2;
			System.out.println("Nombres: "+nombre+" y "+nombre2);
		}
		System.out.println(sql);
		System.out.println(n1);
		System.out.println(n2);
		if(!n1.equalsIgnoreCase("")) {
			return true;
		}
		else
			return false;
	}
	
	public VOEquivalenciaProducto buscarEquivProdPorID(Long id) throws SQLException, Exception {
		VOEquivalenciaProducto prodRetorno = null;

		String sql = "SELECT * FROM EQUIVALENCIA_PRODUCTO WHERE ID ='" + id ;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreProd1 = rs.getString("NOMBRE_PRODUCTO_1");
			String nombreProd2 = rs.getString("NOMBRE_PRODUCTO_2");
			Long id2 = rs.getLong("ID");
			Producto prod1 = darProducto(nombreProd1);
			Producto prod2 = darProducto(nombreProd2);
			prodRetorno = new VOEquivalenciaProducto(prod1, prod2, id2);
		}

		return prodRetorno;
	}
	
	public void addEquivProd(VOEquivalenciaProducto equiv) throws SQLException, Exception {
		
		String sql2 = "INSERT INTO EQUIVALENCIA_PRODUCTO VALUES ('"+equiv.getProducto1().getNombre()+"', '"+equiv.getProducto2().getNombre()+"', "+equiv.getId()+")";
		//INSERT INTO EQUIVALENCIA_PRODUCTO VALUES('Producto 1', 'Producto 2', 1);

		PreparedStatement prepStmt = conn.prepareStatement(sql2);
		System.out.println("SQL 2:"+sql2);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public void deleteEquivProd(VOEquivalenciaProducto equiv) throws SQLException, Exception {

		String sql = "DELETE FROM EQUIVALENCIA_PRODUCTO";
		sql += " WHERE ID = " + equiv.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
