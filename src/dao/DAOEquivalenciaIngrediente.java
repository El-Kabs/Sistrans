package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Categoria;
import vos.Ingrediente;
import vos.Producto;
import vos.VOEquivalenciaIngrediente;
import vos.VOEquivalenciaProducto;

public class DAOEquivalenciaIngrediente {
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
	public DAOEquivalenciaIngrediente() {
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

	public ArrayList<VOEquivalenciaIngrediente> darEquivalenciaIngre() throws SQLException, Exception {
		ArrayList<VOEquivalenciaIngrediente> ingredientes = new ArrayList<VOEquivalenciaIngrediente>();

		String sql = "SELECT * FROM EQUIVALENCIA_INGREDIENTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String ingre1 = rs.getString("NOMBRE_INGREDIENTE_1");
			String ingre2 = rs.getString("NOMBRE_INGREDIENTE_2");
			Long id = rs.getLong("ID");
			Ingrediente prod1 = darIngrediente(ingre1);
			Ingrediente prod2 = darIngrediente(ingre1);
			VOEquivalenciaIngrediente vo = new VOEquivalenciaIngrediente(prod1, prod2, id);
			ingredientes.add(vo);
		}
		return ingredientes;
	}

	public Ingrediente darIngrediente(String nombreP) throws SQLException, Exception {
		String sql = "SELECT * FROM INGREDIENTE WHERE NOMBRE = '"+nombreP+"'";
		Ingrediente retorno = null;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String descripcion = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			retorno = new Ingrediente(nombre, descripcion, traduccion);
		}
		return retorno;
	}

	public VOEquivalenciaIngrediente buscarEquivIngrePorID(Long id) throws SQLException, Exception {
		VOEquivalenciaIngrediente ingreRetorno = null;

		String sql = "SELECT * FROM EQUIVALENCIA_INGREDIENTE WHERE ID ='" + id ;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreProd1 = rs.getString("NOMBRE_INGREDIENTE_1");
			String nombreProd2 = rs.getString("NOMBRE_INGREDIENTE_2");
			Long id2 = rs.getLong("ID");
			Ingrediente prod1 = darIngrediente(nombreProd1);
			Ingrediente prod2 = darIngrediente(nombreProd2);
			ingreRetorno = new VOEquivalenciaIngrediente(prod1, prod2, id2);
		}

		return ingreRetorno;
	}

	public void addEquivIngre(VOEquivalenciaIngrediente equiv) throws SQLException, Exception {

		String sql2 = "INSERT INTO EQUIVALENCIA_INGREDIENTE VALUES ('"+equiv.getIngrediente1().getNombre()+"', '"+equiv.getIngrediente2().getNombre()+"', "+equiv.getId()+")";
		//INSERT INTO EQUIVALENCIA_PRODUCTO VALUES('Ingrediente 1', 'Ingrediente 2', 1);

		PreparedStatement prepStmt = conn.prepareStatement(sql2);
		System.out.println("SQL 2:"+sql2);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public boolean esEquivalenteIngrediente(Ingrediente p1, Ingrediente p2) throws SQLException, Exception {
		String sql = "SELECT * FROM EQUIVALENCIA_INGREDIENTE WHERE NOMBRE_INGREDIENTE_1 = '"+p1.getNombre()+"' AND NOMBRE_INGREDIENTE_2='"+p2.getNombre()+"'";
		String n1 = "";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE_INGREDIENTE_1");
			n1 = nombre;
		}
		if(!n1.equalsIgnoreCase("")) {
			return true;
		}
		else
			return false;
	}

	public void deleteEquivProd(VOEquivalenciaProducto equiv) throws SQLException, Exception {

		String sql = "DELETE FROM EQUIVALENCIA_INGREDIENTE";
		sql += " WHERE ID = " + equiv.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
