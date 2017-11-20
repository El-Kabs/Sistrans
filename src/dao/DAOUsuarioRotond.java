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
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author Monitores 2017-20
 */
public class DAOUsuarioRotond {


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
	public DAOUsuarioRotond() {
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
	public ArrayList<Usuario> darUsuarios() throws SQLException, Exception {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

		String sql = "SELECT * FROM USUARIOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String email = rs.getString("EMAIL");
			String rol = rs.getString("ROL");
			String password = rs.getString("PASSWORD");
			usuarios.add(new Usuario(id, nombre, email, rol, password));
		}
		return usuarios;
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	public ArrayList<VOUsuarioConsulta> darClientes() throws SQLException
	{
		ArrayList<VOUsuarioConsulta> usuarios= new ArrayList<>();
		String sql= "SELECT * FROM(SELECT ID_USUARIO,EMAIL,NOMBRE,ROL,PASSWORD,CATEGORIA,PRECIO_MIN,PRECIO_MAX,ZONA,ID_PREFERENCIA FROM USUARIOS JOIN PREFERENCIA ON USUARIOS.ID= PREFERENCIA.ID_USUARIO)t1 JOIN PEDIDO ON t1.ID_USUARIO=PEDIDO.ID_USUARIO WHERE ROL='UsuarioRegistrado'";
		System.out.println(sql);
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next())
		{
			System.out.println("ENTREEEEEEEEEE");
			boolean yaEstaPreferencia=false;
			boolean yaEstaPedido=false;
			List<Pedido> peds= new ArrayList<>();
			List<Preferencia> prefs= new ArrayList<>();
			Integer idUsuario=rs.getInt("ID_USUARIO");
			String email= rs.getString("EMAIL");
			String nombre=rs.getString("NOMBRE");
			String rol= rs.getString("ROL");
			String password= rs.getString("PASSWORD");
			Categoria categoria=Categoria.valueOf(rs.getString("CATEGORIA"));
			double precioMin=rs.getDouble("PRECIO_MIN");
			double precioMax= rs.getDouble("PRECIO_MAX");
			String zona= rs.getString("ZONA");
			Integer idPreferencia=rs.getInt("ID_PREFERENCIA");
			Integer idPedido= rs.getInt("ID");
			double costoTotal=rs.getDouble("COSTO_TOTAL");
			Date fecha= rs.getDate("FECHA");
			String estado= rs.getString("ESTADO");
			Usuario usu= new Usuario(Long.valueOf(idUsuario), nombre, email, rol, password);
			Preferencia preferencia= new Preferencia(idPreferencia, zona, precioMin, precioMax,categoria, idUsuario);
			Pedido pedido= new Pedido(Long.valueOf(idPedido), costoTotal, Long.valueOf(idUsuario), estado, fecha);
			for (Iterator iterator = usuarios.iterator(); iterator.hasNext();) {
				VOUsuarioConsulta voUsuarioConsulta = (VOUsuarioConsulta) iterator.next();
				if (voUsuarioConsulta.getUsuario().getId()==Long.valueOf(idUsuario)){
					peds=voUsuarioConsulta.getPedidos();
					prefs=voUsuarioConsulta.getPreferencias();
				}
			}
			for (Iterator iterator = prefs.iterator(); iterator.hasNext();) {
				Preferencia prefActual = (Preferencia) iterator.next();
				if(prefActual.getId()==idPreferencia)
				{
					yaEstaPreferencia=true;
				}
			}
			for (Iterator iterator = peds.iterator(); iterator.hasNext();) {
				Pedido pedi = (Pedido) iterator.next();
				if(pedi.getId()==Long.valueOf(idPedido))
				{
					yaEstaPedido=true;
				}
			}
			if(!yaEstaPedido)
			{
				peds.add(pedido);
			}
			if(!yaEstaPreferencia)
			{
				prefs.add(preferencia);
			}
			VOUsuarioConsulta co=new VOUsuarioConsulta(usu, peds, 0.0, prefs);
			if(usuarios.size()!=0)
			{
				for (Iterator iterator = usuarios.iterator(); iterator.hasNext();) {
					VOUsuarioConsulta usuCons = (VOUsuarioConsulta) iterator.next();
					if(usuCons.getUsuario().getId()==co.getUsuario().getId()) {
						usuarios.remove(usuCons);
						usuarios.add(co);
					}

				}
			}
			else
			{
				usuarios.add(co);
			}

		}
		return usuarios;
	}


	/**
	 * Metodo que busca el/los videos con el nombre que entra como parametro.
	 * @param name - Nombre de el/los videos a buscar
	 * @return ArrayList con los videos encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Usuario> buscarUsuariosPorName(String name) throws SQLException, Exception {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

		String sql = "SELECT * FROM USUARIOS WHERE NAME ='" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String email = rs.getString("EMAIL");
			String rol = rs.getString("ROL");
			String password = rs.getString("PASSWORD");
			usuarios.add(new Usuario(id, name2, email, rol, password));
		}

		return usuarios;
	}

	/**
	 * Metodo que busca el video con el id que entra como parametro.
	 * @param name - Id de el video a buscar
	 * @return Video encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Usuario buscarUsuarioPorId(Long id) throws SQLException, Exception 
	{
		Usuario usuario = null;

		String sql = "SELECT * FROM USUARIOS WHERE ID =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String email = rs.getString("EMAIL");
			String rol = rs.getString("ROL");
			String password = rs.getString("PASSWORD");
			usuario = (new Usuario(id2, name, email, rol, password));
		}

		return usuario;
	}

	/**
	 * Metodo que agrega el video que entra como parametro a la base de datos.
	 * @param usuario - el video a agregar. video !=  null
	 * <b> post: </b> se ha agregado el video a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que el video baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "INSERT INTO USUARIOS VALUES (";
		sql += usuario.getId() + " , '";
		sql += "'"+usuario.getNombre() + "',";
		sql += "'"+usuario.getEmail() + "',";
		sql += "'"+usuario.getRol() + "',";
		sql += "'"+usuario.getPassword() + ")";

		String sql2 = "INSERT INTO USUARIOS VALUES ("+usuario.getId()+", '"+usuario.getNombre()+"', '"+usuario.getEmail()+"', '"+usuario.getRol()+"', '"+usuario.getPassword()+"')";
		//		INSERT INTO USUARIOS VALUES(1, 'Kobs', 'kobs@kobs.com', 'admin', 'kabska83');

		PreparedStatement prepStmt = conn.prepareStatement(sql2);
		System.out.println("SQL 1:"+sql);
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
	public void updateUsuario(Usuario usuario) throws SQLException, Exception {

		String sql2 = "UPDATE USUARIOS SET NOMBRE = '"+usuario.getNombre()+"', EMAIL='"+usuario.getEmail()+"', ROL='"+usuario.getRol()+"', PASSWORD='"+usuario.getPassword()+"' WHERE ID="+usuario.getId()
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
	public void deleteUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "DELETE FROM USUARIOS";
		sql += " WHERE ID = " + usuario.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	public ArrayList<VOUsuarioProducto> consultarConsumo(String restaurante,String criterio,String funcion,String fechaInic,String fechaFin) throws SQLException
	{
		System.out.println("ENTRA A CONSULTAR EN DAO");
		ArrayList<VOUsuarioProducto> usuarios= new ArrayList<>();
		if(restaurante!=null)
		{
			String sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"') AND NOMBRE_RESTAURANTE='"+restaurante+"'";
			if(funcion!=null && criterio!=null)
			{
				if(funcion.equals("group"))
				{
					if(criterio.equals("usuario"))
					{
						sql+="GROUP BY (ID_USUARIO,EMAIL,PASSWORD,ROL,NOMBRE)";
					}
					else if(criterio.equals("producto"))
					{
						System.out.println("GROUP BY PRODUCTO");
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE,NOMBRE_PRODUCTO FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"') AND NOMBRE_RESTAURANTE='"+restaurante+"'"+"GROUP BY(ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE,NOMBRE_PRODUCTO)";
						System.out.println(sql);
					}
					else
					{
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE_USUARIO,NOMBRE_PRODUCTO,CATEGORIA FROM(SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE NOMBRE_USUARIO,NOMBRE_PRODUCTO,NOMBRE_RESTAURANTE,FECHA FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO)SIN_CATEG JOIN PRODUCTO ON PRODUCTO.NOMBRE=SIN_CATEG.NOMBRE_PRODUCTO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"') AND NOMBRE_RESTAURANTE='"+restaurante+"'GROUP BY(ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE_USUARIO,NOMBRE_PRODUCTO,CATEGORIA)";
					}
				}
				else
				{
					if(criterio.equals("usuario"))
					{
						sql+="ORDER BY(NOMBRE)";
					}
					else if(criterio.equals("producto"))
					{
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE,NOMBRE_PRODUCTO FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"') AND NOMBRE_RESTAURANTE='"+restaurante+"'"+"ORDER BY (NOMBRE_PRODUCTO)";
					}
					else
					{
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE_USUARIO,NOMBRE_PRODUCTO,CATEGORIA FROM(SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE NOMBRE_USUARIO,NOMBRE_PRODUCTO,NOMBRE_RESTAURANTE,FECHA FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO)SIN_CATEG JOIN PRODUCTO ON PRODUCTO.NOMBRE=SIN_CATEG.NOMBRE_PRODUCTO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"') AND NOMBRE_RESTAURANTE='"+restaurante+"'ORDER BY (CATEGORIA)";
					}
				}
			}
			PreparedStatement prpStrmt= conn.prepareStatement(sql);
			recursos.add(prpStrmt);
			ResultSet rs=prpStrmt.executeQuery();
			while(rs.next())
			{
				String nombre=null;
				try {
					nombre= rs.getString("NOMBRE");
				}
				catch(Exception e)
				{

				}
				try {
					 nombre = rs.getString("NOMBRE_USUARIO");
				}
				catch(Exception e)
				{

				}
				Long id = rs.getLong("ID_USUARIO");
				String email = rs.getString("EMAIL");
				String rol = rs.getString("ROL");
				String password = rs.getString("PASSWORD");
				String producto=null;
				try {
					producto= rs.getString("NOMBRE_PRODUCTO");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				String categProducto=null;
				try {
					categProducto=rs.getString("CATEGORIA");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				Usuario usuario=new Usuario(id, nombre, email, rol, password);		
				VOUsuarioProducto vo=new VOUsuarioProducto(usuario, producto, categProducto);
				usuarios.add(vo);
			}
		}
		else
		{
			String sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"')";
			if(funcion!=null && criterio!=null)
			{
				if(funcion.equals("group"))
				{
					if(criterio.equals("usuario"))
					{
						sql+="GROUP BY (ID_USUARIO,EMAIL,PASSWORD,ROL,NOMBRE)";
					}
					else if(criterio.equals("producto"))
					{
						System.out.println("ENTRA A PRODUCTO SIN RESTAURANTE");
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE,NOMBRE_PRODUCTO FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"')"+"GROUP BY(ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE,NOMBRE_PRODUCTO)";
					}
					else
					{
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE_USUARIO,NOMBRE_PRODUCTO,CATEGORIA FROM(SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE NOMBRE_USUARIO,NOMBRE_PRODUCTO,NOMBRE_RESTAURANTE,FECHA FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO)SIN_CATEG JOIN PRODUCTO ON PRODUCTO.NOMBRE=SIN_CATEG.NOMBRE_PRODUCTO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"')GROUP BY(ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE_USUARIO,NOMBRE_PRODUCTO,CATEGORIA)";
					}
				}
				else
				{
					if(criterio.equals("usuario"))
					{
						sql+="ORDER BY(NOMBRE)";
					}
					else if(criterio.equals("producto"))
					{
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE,NOMBRE_PRODUCTO FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"') AND NOMBRE_RESTAURANTE='"+restaurante+"'"+"ORDER BY (NOMBRE_PRODUCTO)";
					}
					else
					{
						sql="SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE_USUARIO,NOMBRE_PRODUCTO,CATEGORIA FROM(SELECT ID_USUARIO, EMAIL, PASSWORD, ROL,NOMBRE NOMBRE_USUARIO,NOMBRE_PRODUCTO,NOMBRE_RESTAURANTE,FECHA FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO)SIN_CATEG JOIN PRODUCTO ON PRODUCTO.NOMBRE=SIN_CATEG.NOMBRE_PRODUCTO WHERE (FECHA BETWEEN '"+fechaInic+"' AND '"+fechaFin+"')ORDER BY (CATEGORIA)";
					}
				}
			}
			PreparedStatement prpStrmt= conn.prepareStatement(sql);
			recursos.add(prpStrmt);
			ResultSet rs=prpStrmt.executeQuery();
			while(rs.next())
			{
				String nombre=null;
				try {
					nombre= rs.getString("NOMBRE");
				}
				catch(Exception e)
				{

				}
				try {
					 nombre = rs.getString("NOMBRE_USUARIO");
				}
				catch(Exception e)
				{

				}
				Long id = rs.getLong("ID_USUARIO");
				String email = rs.getString("EMAIL");
				String rol = rs.getString("ROL");
				String password = rs.getString("PASSWORD");
				String producto=null;
				try {
					producto= rs.getString("NOMBRE_PRODUCTO");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				String categProducto=null;
				try {
					categProducto=rs.getString("CATEGORIA");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				Usuario usuario=new Usuario(id, nombre, email, rol, password);		
				VOUsuarioProducto vo=new VOUsuarioProducto(usuario, producto, categProducto);
				usuarios.add(vo);
			}
		}
		return usuarios;
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	public ArrayList<Usuario> consultarNoConsumo(String restaurante,String fechaInic,String fechaFin) throws SQLException
	{
		ArrayList<Usuario> usuarios= new ArrayList<>();
		String sql=null;
		if(restaurante!=null)
		{
			 sql="SELECT * FROM USUARIOS WHERE ID NOT IN (SELECT ID_USUARIO FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE FECHA BETWEEN ('"+fechaInic+"' AND '"+fechaFin+"')AND NOMBRE_RESTAURANTE='"+restaurante+"'";
		}
		else
		{
			 sql="SELECT * FROM USUARIOS WHERE ID NOT IN (SELECT ID_USUARIO FROM(SELECT * FROM (SELECT * FROM  PEDIDO_PRODUCTO JOIN PEDIDO ON ID_PEDIDO=ID)PEDIDOS NATURAL JOIN RESTAURANTE_PRODUCTO)PEDIDOS_USUARIO JOIN USUARIOS ON USUARIOS.ID=PEDIDOS_USUARIO.ID_USUARIO WHERE FECHA BETWEEN ('"+fechaInic+"' AND '"+fechaFin+"')";
		}
		PreparedStatement prpStmt= conn.prepareStatement(sql);
		ResultSet rs=prpStmt.executeQuery();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String email = rs.getString("EMAIL");
			String rol = rs.getString("ROL");
			String password = rs.getString("PASSWORD");
			usuarios.add(new Usuario(id, nombre, email, rol, password));
		}
		
		return usuarios;
	}
}
