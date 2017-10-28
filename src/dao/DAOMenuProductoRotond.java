package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Categoria;
import vos.Menu;
import vos.MenuProducto;
import vos.PedidoMenu;
import vos.PedidoProducto;
import vos.Producto;


public class DAOMenuProductoRotond {
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOMenuProductoRotond() {
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
	
	public List<Producto> darProductosMenu(Menu menu) throws SQLException
	{
		ArrayList<Producto> resp= new ArrayList<>();
		
		String sql= "SELECT * FROM MENU_PRODUCTO WHERE ID_MENU=" + menu.getId();
		PreparedStatement prpStmt=conn.prepareStatement(sql);
		recursos.add(prpStmt);
		ResultSet rs= prpStmt.executeQuery();
		
		 while(rs.next())
		 {
			 String sql2= "SELECT * FROM PRODUCTO WHERE NOMBRE='" + rs.getString("NOMBRE_PRODUCTO")+"'";
				PreparedStatement prpStmt2=conn.prepareStatement(sql2);
				recursos.add(prpStmt2);
				ResultSet rs2= prpStmt2.executeQuery();
				while (rs2.next()) {
					String nombre = rs2.getString("NOMBRE");
					String info = rs2.getString("INFORMACION");
					String traduccion = rs2.getString("TRADUCCION");
					String preparacion = rs2.getString("PREPARACION");
					double costo = rs2.getDouble("COSTO_PRODUCCION");
					double precio = rs2.getDouble("PRECIO");
					Categoria categoria = Categoria.valueOf(rs2.getString("CATEGORIA"));
					resp.add(new Producto(nombre, info, preparacion, traduccion, costo, precio, categoria));
				}
		 }
		return resp;
	}	
}
