/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: 
 * -------------------------------------------------------------------
 */
package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import dao.DAORestauranteRotond;
import dao.DAOUsuarioRotond;
import dao.DAOZonaRotond;
import dao.DAOEquivalenciaIngrediente;
import dao.DAOEquivalenciaProducto;
import dao.DAOIngredienteRotond;
import dao.DAOMenuProductoRotond;
import dao.DAOMenuRotond;
import dao.DAOPedidoMenuRotond;
import dao.DAOPedidoProductoRotond;
import dao.DAOPedidoRotond;
import dao.DAOPreferenciaRotond;
import dao.DAOProductoIngrediente;
import dao.DAOProductoRotond;
import dao.DAORestauranteProductoRotond;
import vos.Ingrediente;
import vos.Menu;
import vos.Pedido;
import vos.PedidoMenu;
import vos.PedidoMesa;
import vos.PedidoProducto;
import vos.PedidoProductoConEquivalencia;
import vos.Preferencia;
import vos.Producto;
import vos.ProductoIngrediente;
import vos.Restaurante;
import vos.RestauranteProducto;
import vos.Usuario;
import vos.VOConsultaFuncionamiento;
import vos.VOConsultaUsuarioPedidos;
import vos.VOConsultaZona;
import vos.VOEquivalenciaIngrediente;
import vos.VOEquivalenciaProducto;
import vos.VOUsuarioConsulta;
import vos.VOUsuarioProducto;
import vos.Zona;

/**
 * Transaction Manager de la aplicacion (TM)
 * Fachada en patron singleton de la aplicacion
 * @author 
 */
public class RotondAndesTM {


	/**
	 * Atributo estatico que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;

	/**
	 * conexion a la base de datos
	 */
	private Connection conn;


	/**
	 * Metodo constructor de la clase VideoAndesMaster, esta clase modela y contiene cada una de las 
	 * transacciones y la logica de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto VideoAndesTM, se inicializa el path absoluto del archivo de conexion y se
	 * inicializa los atributos que se usan par la conexion a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public RotondAndesTM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/**
	 * Metodo que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexion a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que  retorna la conexion a la base de datos
	 * @return Connection - la conexion a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		
		return DriverManager.getConnection(url, user, password);
	}

	/////////////////////////////////////////////////
	///////Transacciones USUARIOS////////////////////
	/////////////////////////////////////////////////


	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Usuario> darUsuarios() throws Exception {
		List<Usuario> usuarios;
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			usuarios = daoRotond.darUsuarios();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}

	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Usuario> buscarUsuariosPorName(String name) throws Exception {
		List<Usuario> videos;
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			videos = daoRotond.buscarUsuariosPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return videos;
	}

	/**
	 * Metodo que modela la transaccion que busca el video en la base de datos con el id que entra como parametro.
	 * @param name - Id del video a buscar. name != null
	 * @return Video - Resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Usuario buscarUsuarioPorId(Long id) throws Exception {
		Usuario video;
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			video = daoRotond.buscarUsuarioPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return video;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */
	public void addUsuario(Usuario usuario) throws Exception {
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.addUsuario(usuario);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los videos que entran como parametro
	 * @param usuarios - objeto que modela una lista de videos y se estos se pretenden agregar. videos != null
	 * @throws Exception - cualquier error que se genera agregando los videos
	 */
	public void addUsuarios(List<Usuario> usuarios) throws Exception {
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			Iterator<Usuario> it = usuarios.iterator();
			while(it.hasNext())
			{
				daoRotond.addUsuario(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updateUsuario(Usuario usuario) throws Exception {
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updateUsuario(usuario);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void deleteUsuario(Usuario usuario) throws Exception {
		DAOUsuarioRotond daoVideos = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deleteUsuario(usuario);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<VOUsuarioConsulta> darClientes() throws SQLException
	{
		List<VOUsuarioConsulta> usuarios;
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			usuarios = daoRotond.darClientes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}
	
	public VOConsultaUsuarioPedidos consultarPedidos(Long id) throws SQLException
	{
		List<VOConsultaUsuarioPedidos> pedidos;
		DAOPedidoProductoRotond daoRotond = new DAOPedidoProductoRotond();
		DAOPedidoMenuRotond daoRotond2= new DAOPedidoMenuRotond();
		VOConsultaUsuarioPedidos consulta=null;
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond2.setConn(conn);			
			ArrayList<PedidoProducto> pedidosProd= daoRotond.consultarPeddidosUsuario(id);
			ArrayList<PedidoMenu> pedidosMenu= daoRotond2.getPedidosMenuUsuario(id);
			consulta=new VOConsultaUsuarioPedidos(pedidosMenu, pedidosProd);
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return consulta;
	}
	
	
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones PRODUCTOS///////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */

	public List<Producto> darProductos() throws Exception {
		List<Producto> productos;
		DAOProductoRotond daoRotond = new DAOProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			productos = daoRotond.darProductos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Producto> buscarProductosPorName(String name) throws Exception {
		List<Producto> productos;
		DAOProductoRotond daoRotond = new DAOProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			productos = daoRotond.buscarProductoPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */

	public void addProducto(Producto producto) throws Exception {
		DAOProductoRotond daoRotond = new DAOProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.addProducto(producto);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los videos que entran como parametro
	 * @param usuarios - objeto que modela una lista de videos y se estos se pretenden agregar. videos != null
	 * @throws Exception - cualquier error que se genera agregando los videos
	 */
	public void addProductos(List<Producto> producto) throws Exception {
		DAOProductoRotond daoRotond = new DAOProductoRotond();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoRotond.setConn(conn);
			Iterator<Producto> it = producto.iterator();
			while(it.hasNext())
			{
				daoRotond.addProducto(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updateProducto(Producto producto) throws Exception {
		DAOProductoRotond daoRotond = new DAOProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updateProducto(producto);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */

	public void deleteProducto(Producto producto) throws Exception {
		DAOProductoRotond daoVideos = new DAOProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deleteProducto(producto);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */

	//---------------------------------------------------------------------------------------------------------------------------
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones RESTAURANTES////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Restaurante> darRestaurantes() throws Exception {
		List<Restaurante> restaurantes;
		DAORestauranteRotond daoRotond = new DAORestauranteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			restaurantes = daoRotond.darRestaurante();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}
	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Restaurante> buscarRestaurantePorNombre(String name) throws Exception {
		List<Restaurante> restaurantes;
		DAORestauranteRotond daoRotond = new DAORestauranteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			restaurantes = daoRotond.buscarRestaurantesPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}
	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */
	public void addRestaurante(Restaurante restaurante) throws Exception {
		DAORestauranteRotond daoRotond = new DAORestauranteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRotond.setConn(conn);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.addRestaurante(restaurante);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los videos que entran como parametro
	 * @param usuarios - objeto que modela una lista de videos y se estos se pretenden agregar. videos != null
	 * @throws Exception - cualquier error que se genera agregando los videos
	 */
	public void addRestaurantes(List<Restaurante> restaurantes) throws Exception {
		DAORestauranteRotond daoRotond = new DAORestauranteRotond();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			Iterator<Restaurante> it = restaurantes.iterator();
			while(it.hasNext())
			{
				daoRotond.addRestaurante(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void deleteRestaurante(Restaurante restaurante) throws Exception {
		DAORestauranteRotond daoVideos = new DAORestauranteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deleteRestaurante(restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updateRestaurante(Restaurante restaurante) throws Exception {
		DAORestauranteRotond daoRotond = new DAORestauranteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updateRestaurante(restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void reabastecerRestaurante(RestauranteProducto restaurante) throws SQLException
	{
		DAORestauranteProductoRotond daoRotond = new DAORestauranteProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.reabastecer(restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones ZONA/////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */

	public List<Zona> darZonas() throws Exception {
		List<Zona> zonas;
		DAOZonaRotond daoRotond = new DAOZonaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			zonas = daoRotond.darZona();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zonas;
	}

	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Zona> buscarZonaPorNombre(String name) throws Exception {
		List<Zona> zonas;
		DAOZonaRotond daoRotond = new DAOZonaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			zonas = daoRotond.buscarZonasPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zonas;
	}
	/**
	 * 
	 */
	public List<VOConsultaZona> darZonaInfoPorNombre(String name) throws Exception {
		List<VOConsultaZona> zonas;
		DAOZonaRotond daoRotond = new DAOZonaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			zonas = daoRotond.darZonaConInfo(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zonas;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */

	public void addZona(Zona zona) throws Exception {
		DAOZonaRotond daoRotond = new DAOZonaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.addZona(zona);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los videos que entran como parametro
	 * @param usuarios - objeto que modela una lista de videos y se estos se pretenden agregar. videos != null
	 * @throws Exception - cualquier error que se genera agregando los videos
	 */
	public void addZonas(List<Zona> zonas) throws Exception {
		DAOZonaRotond daoRotond = new DAOZonaRotond();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			Iterator<Zona> it = zonas.iterator();
			while(it.hasNext())
			{
				daoRotond.addZona(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */

	public void deleteZona(Zona zona) throws Exception {
		DAOZonaRotond daoVideos = new DAOZonaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deleteZona(zona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones IGREDIENTE/////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */

	public List<Ingrediente> darIngredientes() throws Exception {
		List<Ingrediente> ingredientes;
		DAOIngredienteRotond daoRotond = new DAOIngredienteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			ingredientes = daoRotond.darIngredientes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingredientes;
	}

	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Ingrediente> buscarIngredientesPorName(String name) throws Exception {
		List<Ingrediente> ingredientes;
		DAOIngredienteRotond daoRotond = new DAOIngredienteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			ingredientes = daoRotond.buscarIngredientesPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingredientes;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */

	public void addIngrediente(Ingrediente usu) throws Exception {
		DAOIngredienteRotond daoRotond = new DAOIngredienteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.addIngrediente(usu);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los videos que entran como parametro
	 * @param usuarios - objeto que modela una lista de videos y se estos se pretenden agregar. videos != null
	 * @throws Exception - cualquier error que se genera agregando los videos
	 */
	public void addIngredientes(List<Ingrediente> ingrediente) throws Exception {
		DAOIngredienteRotond daoRotond = new DAOIngredienteRotond();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			Iterator<Ingrediente> it = ingrediente.iterator();
			while(it.hasNext())
			{
				daoRotond.addIngrediente(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updateIngrediente(Ingrediente ingrediente) throws Exception {
		DAOIngredienteRotond daoRotond = new DAOIngredienteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updateIngrediente(ingrediente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */

	public void deleteIngrediente(Ingrediente ingrediente) throws Exception {
		DAOIngredienteRotond daoVideos = new DAOIngredienteRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deleteIngrediente(ingrediente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones Preferencias////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Preferencia> darPreferencias(Integer id) throws Exception {
		List<Preferencia> preferencias;
		DAOPreferenciaRotond daoRotond = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			preferencias = daoRotond.darPreferencia(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return preferencias;
	}
	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param Id - id del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Preferencia buscarPreferenciaPorId(Integer idPreferencia) throws Exception {
		Preferencia restaurantes;
		DAOPreferenciaRotond daoRotond = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			restaurantes = daoRotond.buscarPreferenciaPorId(idPreferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}

	public Preferencia buscarPreferenciaPorIdUsuario(Integer idUsuario) throws Exception {
		Preferencia restaurantes;
		DAOPreferenciaRotond daoRotond = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			restaurantes = daoRotond.buscarPreferenciaDeUnUsuario(idUsuario);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}

	public Preferencia buscarPreferenciaEspecificaPorIdUsuario(Integer idUsuario, Integer idPreferencia) throws Exception {
		Preferencia restaurantes;
		DAOPreferenciaRotond daoRotond = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			restaurantes = daoRotond.buscarPreferenciaEspecificaDeUnUsuario(idUsuario, idPreferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}
	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */
	public void addPreferencia(Preferencia preferencia) throws Exception {
		DAOPreferenciaRotond daoRotond = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.addPreferencia(preferencia);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */

	public void deletePreferencia(Preferencia preferencia) throws Exception {
		DAOPreferenciaRotond daoVideos = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deletePreferencia(preferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void deleteRestaurante(Preferencia preferencia) throws Exception {
		DAOPreferenciaRotond daoVideos = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deletePreferencia(preferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updatePreferencia(Preferencia preferencia) throws Exception {
		DAOPreferenciaRotond daoRotond = new DAOPreferenciaRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updatePreferencia(preferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones MENU////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */

	public List<Menu> darMenus() throws Exception {
		List<Menu> menus;
		DAOMenuRotond daoRotond = new DAOMenuRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			menus = daoRotond.darMenus();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menus;
	}

	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Menu> buscarMenuPorName(String name) throws Exception {
		List<Menu> menus;
		DAOMenuRotond daoRotond = new DAOMenuRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			menus = daoRotond.buscarMenusPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menus;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */

	public void addMenu(Menu menu) throws Exception {
		DAOMenuRotond daoRotond = new DAOMenuRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.addMenu(menu);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los videos que entran como parametro
	 * @param usuarios - objeto que modela una lista de videos y se estos se pretenden agregar. videos != null
	 * @throws Exception - cualquier error que se genera agregando los videos
	 */
	public void addMenus(List<Menu> menus) throws Exception {
		DAOMenuRotond daoRotond = new DAOMenuRotond();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			Iterator<Menu> it = menus.iterator();
			while(it.hasNext())
			{
				daoRotond.addMenu(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updateMenu(Menu menu) throws Exception {
		DAOMenuRotond daoRotond = new DAOMenuRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updateMenu(menu);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */

	public void deleteMenu(Menu menu) throws Exception {
		DAOMenuRotond daoVideos = new DAOMenuRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deleteMenu(menu);
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones PRODUCTOPEDIDO//////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	public List<PedidoProducto> darPedidoProductos() throws Exception {
		List<PedidoProducto> pedidosProductos;
		DAOPedidoProductoRotond daoRotond = new DAOPedidoProductoRotond();
		DAOProductoRotond daoProducto = new DAOProductoRotond();
		DAOPedidoRotond daoPedido = new DAOPedidoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoPedido.setConn(conn);
			daoProducto.setConn(conn);
			pedidosProductos = daoRotond.darPedidoProducto();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidosProductos;
	}

	public void addPedidoProducto(PedidoProducto pedidoProducto) throws Exception {
		DAOPedidoProductoRotond daoRotond = new DAOPedidoProductoRotond();
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();
		DAORestauranteRotond restauranteDAO = new DAORestauranteRotond();
		DAORestauranteProductoRotond productoRestauranteDAO = new DAORestauranteProductoRotond();
		DAOMenuRotond menu = new DAOMenuRotond();
		DAOMenuProductoRotond menuP = new DAOMenuProductoRotond();


		try 
		{
			//////transaccion
			this.conn = darConexion();
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			pedidoDao.setConn(conn);
			productoDAO.setConn(conn);
			productoRestauranteDAO.setConn(conn);
			menu.setConn(conn);
			menuP.setConn(conn);
			
			

			ArrayList<Menu> menus = new ArrayList<>();
			double costoTotal = 0;
			for(int i = 0; i < pedidoProducto.getProducto().size(); i++) {
				if(!menu.buscarMenusPorName(pedidoProducto.getProducto().get(i).getNombre()).isEmpty()) {
					menus.add(menu.buscarMenusPorName(pedidoProducto.getProducto().get(i).getNombre()).get(0));
				}
			}
			ArrayList<Producto> disponibles = new ArrayList<>();
			for(int i = 0; i<menus.size(); i++) {
				List<Producto> productosM = menuP.darProductosMenu(menus.get(0));
				for(int j = 0; j<productosM.size(); j++) {
					RestauranteProducto productoVerif = productoRestauranteDAO.buscarRestauranteProductoPorNameProducto(productosM.get(j).getNombre());
					if(productoVerif!=null)
					{
						if(productoVerif.getCantidad()>=1) {
							disponibles.add(productoVerif.getProducto());
						}
					}
				}
			}

			for(int i = 0; i<pedidoProducto.getProducto().size(); i++){
				RestauranteProducto productoVerif = productoRestauranteDAO.buscarRestauranteProductoPorNameProducto(pedidoProducto.getProducto().get(i).getNombre());
				if(productoVerif!=null)
				{
					if(productoVerif.getCantidad()>=1) {
						disponibles.add(productoVerif.getProducto());
					}
				}
			}
			pedidoDao.addPedido(pedidoProducto.getPedido());
			if(pedidoDao.buscarPedidoPorId(pedidoProducto.getPedido().getId())!=null) {
				PedidoProducto agregar = new PedidoProducto(disponibles, pedidoProducto.getPedido());
				daoRotond.addPedidoProducto(agregar);
			}
			for(int i=0;i<disponibles.size(); i++) {
				RestauranteProducto prod = productoRestauranteDAO.buscarRestauranteProductoPorNameProducto(disponibles.get(i).getNombre());
				productoRestauranteDAO.actualizarCantidad(prod);
				costoTotal+=disponibles.get(i).getPrecio();
			}
			pedidoDao.updatePedidoCosto(pedidoProducto.getPedido(), costoTotal);

			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addPedidoProductoEquivalencias(PedidoProductoConEquivalencia pedidoProducto) throws Exception {
		DAOPedidoProductoRotond daoRotond = new DAOPedidoProductoRotond();
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();
		DAORestauranteRotond restauranteDAO = new DAORestauranteRotond();
		DAORestauranteProductoRotond productoRestauranteDAO = new DAORestauranteProductoRotond();
		DAOEquivalenciaProducto daoEquiv = new DAOEquivalenciaProducto();
		DAOProductoIngrediente daoProdIngrediente = new DAOProductoIngrediente();
		DAOEquivalenciaIngrediente daoEquivIngre = new DAOEquivalenciaIngrediente();
		DAOMenuRotond menu = new DAOMenuRotond();
		
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			pedidoDao.setConn(conn);
			productoDAO.setConn(conn);
			daoEquiv.setConn(conn);
			productoRestauranteDAO.setConn(conn);
			daoProdIngrediente.setConn(conn);
			menu.setConn(conn);

			ArrayList<Menu> menus = new ArrayList<>();
			double costoTotal = 0;
			for(int i = 0; i < pedidoProducto.getProducto().size(); i++) {
				if(!menu.buscarMenusPorName(pedidoProducto.getProducto().get(i).getNombre()).isEmpty()) {
					menus.add(menu.buscarMenusPorName(pedidoProducto.getProducto().get(i).getNombre()).get(0));
				}
			}
			ArrayList<Producto> disponibles = new ArrayList<>();
			boolean equivalenciaI = false;
			for(int i = 0; i<pedidoProducto.getProducto().size(); i++){
				RestauranteProducto productoVerif = productoRestauranteDAO.buscarRestauranteProductoPorNameProducto(pedidoProducto.getProducto().get(i).getNombre());
				if(productoVerif!=null)
				{
					if(productoVerif.getCantidad()>=1) {
						disponibles.add(productoVerif.getProducto());
					}
				}
			}
			if(!(pedidoProducto.getEquivalenciasP()==null)) {
				for(int i = 0; i<pedidoProducto.getEquivalenciasP().size(); i++) {
					for(int j = 0; j<disponibles.size(); j++) {
						System.out.println(pedidoProducto.getEquivalenciasP().get(i).getNombre()+" es equivalente con: "+disponibles.get(j).getNombre()+"?");
						System.out.println("Respuesta: "+daoEquiv.esEquivalenteProducto(pedidoProducto.getEquivalenciasP().get(i), disponibles.get(j)));
						if(daoEquiv.esEquivalenteProducto(pedidoProducto.getEquivalenciasP().get(i), disponibles.get(j))) {
							RestauranteProducto productoVerif = productoRestauranteDAO.buscarRestauranteProductoPorNameProducto(pedidoProducto.getEquivalenciasP().get(i).getNombre());
							if(productoVerif!=null)
							{
								if(productoVerif.getCantidad()>=1) {
									disponibles.set(j, pedidoProducto.getEquivalenciasP().get(i));
								}
							}
						}
					}
				}
			}

			if(!(pedidoProducto.getEquivalenciasI()==null)) {
				for(int i = 0; i<pedidoProducto.getEquivalenciasI().size(); i++) {
					for(int j = 0; j<disponibles.size(); j++) {
						ProductoIngrediente ingre = daoProdIngrediente.buscarIngredienteProductoPorNameProducto(disponibles.get(j).getNombre());
						if(daoEquivIngre.esEquivalenteIngrediente(pedidoProducto.getEquivalenciasI().get(i), ingre.getIngrediente())) {
							equivalenciaI = true;
						}
					}
				}
			}
			
			if(pedidoProducto.getEquivalenciasI()==null)
				equivalenciaI=true;

			if(equivalenciaI==true)
			{
				pedidoDao.addPedido(pedidoProducto.getPedido());
				if(pedidoDao.buscarPedidoPorId(pedidoProducto.getPedido().getId())!=null) {
					PedidoProducto agregar = new PedidoProducto(disponibles, pedidoProducto.getPedido());
					daoRotond.addPedidoProducto(agregar);
				}
				for(int i=0;i<disponibles.size(); i++) {
					costoTotal+=disponibles.get(i).getPrecio();
				}
				
				for(int i=0;i<disponibles.size(); i++) {
					RestauranteProducto prod = productoRestauranteDAO.buscarRestauranteProductoPorNameProducto(disponibles.get(i).getNombre());
					productoRestauranteDAO.actualizarCantidad(prod);
					costoTotal+=disponibles.get(i).getPrecio();
				}

				pedidoDao.updatePedidoCosto(pedidoProducto.getPedido(), costoTotal);

				conn.commit();
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				pedidoDao.cerrarRecursos();
				productoDAO.cerrarRecursos();
				productoRestauranteDAO.cerrarRecursos();
				daoEquiv.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	public boolean esEquivalenteP(Producto p1, Producto p2) throws Exception {
		DAOEquivalenciaProducto dao= new DAOEquivalenciaProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			dao.setConn(conn);
			return dao.esEquivalenteProducto(p1, p2);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;

		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	public boolean esEquivalenteI(Ingrediente i1, Ingrediente i2) throws Exception {
		DAOEquivalenciaIngrediente dao= new DAOEquivalenciaIngrediente();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			dao.setConn(conn);
			return dao.esEquivalenteIngrediente(i1, i2);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;

		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	public void addPedidoMenuCompleto(PedidoMenu pedidoMenu) throws Exception
	{
		DAOPedidoMenuRotond menuDao= new DAOPedidoMenuRotond();
		DAOPedidoRotond daoRotond= new DAOPedidoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			menuDao.setConn(conn);
			daoRotond.addPedido(pedidoMenu.getPedido());
			menuDao.addPedidoMenu(pedidoMenu);
			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				menuDao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}		
	}

	public void addPedidoMenu(PedidoMenu pedidoMenu) throws Exception
	{
		DAOPedidoMenuRotond menuDao= new DAOPedidoMenuRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			menuDao.addPedidoMenu(pedidoMenu);
			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				menuDao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}		
	}

	public void addPedidoMesa(PedidoMesa pedido) throws Exception
	{
		List<PedidoProducto> pedidos= pedido.getPedidos();
		for (PedidoProducto pedidoProducto : pedidos) {
			addPedidoProducto(pedidoProducto);
		}
	}

	public void deletePedidoProducto(PedidoProducto pedidoProducto) throws Exception
	{
		DAOPedidoProductoRotond daoRotond= new DAOPedidoProductoRotond();
		try {
			this.conn=darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.deletePedidoProducto(pedidoProducto);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	public void deletePedidoMenu(PedidoMenu pedidoMenu) throws Exception
	{
		DAOPedidoMenuRotond daoRotond= new DAOPedidoMenuRotond();
		try {
			this.conn=darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.deletePedidoMenu(pedidoMenu);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	public void deletePedidoMesa(PedidoMesa pedido) throws Exception
	{
		List<PedidoProducto> pedidos= pedido.getPedidos();
		for (PedidoProducto pedidoProducto : pedidos) {
			deletePedidoProducto(pedidoProducto);
		}
	}

	public PedidoProducto buscarPedidoProductoPorName(String name) throws Exception {
		PedidoProducto pedidoProducto;
		DAOPedidoProductoRotond daoRotond = new DAOPedidoProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			pedidoProducto = daoRotond.buscarPedidoProductoPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidoProducto;
	}

	public PedidoProducto buscarPedidoProductoPorID(Long id) throws Exception {
		PedidoProducto pedidoProducto;
		DAOPedidoProductoRotond daoRotond = new DAOPedidoProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			pedidoProducto = daoRotond.buscarPedidoProductoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidoProducto;
	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones PEDIDO//////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Pedido> darPedido() throws Exception {
		List<Pedido> pedidos;
		DAOPedidoRotond daoRotond = new DAOPedidoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			pedidos = daoRotond.darPedido();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidos;
	}
	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param Id - id del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Pedido buscarPedidoPorId(Integer idPedido) throws Exception {
		Pedido pedido;
		DAOPedidoRotond daoRotond = new DAOPedidoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			pedido = daoRotond.buscarPedidoPorId(Long.valueOf(idPedido));

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedido;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param usuario - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */
	public void addPedido(Pedido pedido) throws Exception {
		DAOPedidoRotond daoRotond = new DAOPedidoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.addPedido(pedido);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param usuario - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */

	public void deletePedido(Pedido pedido) throws Exception {
		DAOPedidoRotond daoVideos = new DAOPedidoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoVideos.setConn(conn);
			daoVideos.deletePedido(pedido);
			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param usuario - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updatePedido(Pedido pedido) throws Exception {
		DAOPedidoRotond daoRotond = new DAOPedidoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updatePedido(pedido);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updatePedidoEstado(PedidoProducto pedido, RestauranteProducto restaurante) throws Exception {
		DAOPedidoRotond daoRotond = new DAOPedidoRotond();
		DAORestauranteProductoRotond daoPR2 = new DAORestauranteProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRotond.updateEstadoPedido(pedido.getPedido());
			for(int i = 0; i<pedido.getProducto().size(); i++){
				RestauranteProducto restaurante2 = new RestauranteProducto(restaurante.getRestaurante(), pedido.getProducto().get(i), restaurante.getCantidad(), restaurante.getMax());
				daoPR2.disminuirCantidad(restaurante2);
			}
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updatePedidoMenuEstado(PedidoMenu pedidoMenu,RestauranteProducto restaurante) throws Exception
	{
		DAOMenuProductoRotond daoRotond = new DAOMenuProductoRotond();
		DAORestauranteProductoRotond daoPR2 = new DAORestauranteProductoRotond();
		try 
		{
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoPR2.setConn(conn);
			List<Producto> prods=daoRotond.darProductosMenu(pedidoMenu.getMenu());
			PedidoProducto pedido= new PedidoProducto(prods, pedidoMenu.getPedido());
			updatePedidoEstado(pedido, restaurante);
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				daoPR2.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updatePedidoMesaEstado()
	{

	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones EQUIV PRODUCTO//////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	public List<VOEquivalenciaProducto> darEquivalenciaProd() throws Exception {
		List<VOEquivalenciaProducto> equivalencias;
		DAOEquivalenciaProducto daoRotond = new DAOEquivalenciaProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			equivalencias = daoRotond.darEquivalenciaProd();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalencias;
	}

	public VOEquivalenciaProducto buscarEquivProdPorId(Integer idEquiv) throws Exception {
		VOEquivalenciaProducto equivalencia;
		DAOEquivalenciaProducto daoRotond = new DAOEquivalenciaProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			equivalencia = daoRotond.buscarEquivProdPorID(Long.valueOf(idEquiv));

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalencia;
	}

	public void addEquivalencia(VOEquivalenciaProducto equivalencia) throws Exception {
		DAOEquivalenciaProducto daoRotond = new DAOEquivalenciaProducto();
		DAORestauranteProductoRotond daoRestProd = new DAORestauranteProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoRestProd.setConn(conn);
			RestauranteProducto RP1 = daoRestProd.buscarRestauranteProductoPorNameProducto(equivalencia.getProducto1().getNombre());
			RestauranteProducto RP2 = daoRestProd.buscarRestauranteProductoPorNameProducto(equivalencia.getProducto2().getNombre());
			if(RP1.getRestaurante().getNombre().equals(RP2.getRestaurante().getNombre())) {
				daoRotond.addEquivProd(equivalencia);
				conn.commit();
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				daoRestProd.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones Resta PRODUCTO//////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	public List<RestauranteProducto> darRestauranteProd() throws Exception {
		List<RestauranteProducto> restProductos;
		DAORestauranteProductoRotond daoRotond = new DAORestauranteProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			restProductos = daoRotond.darRestauranteProducto();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restProductos;
	}

	public void addRestauranteProducto(RestauranteProducto restProducto) throws Exception {
		DAORestauranteProductoRotond daoRestProd = new DAORestauranteProductoRotond();
		DAORestauranteRotond daoRestaurante = new DAORestauranteRotond();
		DAOProductoRotond daoProducto = new DAOProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRestProd.setConn(conn);
			daoRestaurante.setConn(conn);
			daoProducto.setConn(conn);
			if(daoRestaurante.buscarRestaurantesPorName(restProducto.getRestaurante().getNombre())!=null&&daoProducto.buscarProductoPorName(restProducto.getProducto().getNombre())!=null) {
				restProducto.setProducto(daoProducto.buscarProductoPorName(restProducto.getProducto().getNombre()).get(0));
				restProducto.setRestaurante(daoRestaurante.buscarRestaurantesPorName(restProducto.getRestaurante().getNombre()).get(0));
				daoRestProd.addRestauranteProducto(restProducto);
				conn.commit();
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestProd.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones EQUIV INGREDIENTE///////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	public List<VOEquivalenciaIngrediente> darEquivalenciaIngre() throws Exception {
		List<VOEquivalenciaIngrediente> equivalencias;
		DAOEquivalenciaIngrediente daoRotond = new DAOEquivalenciaIngrediente();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			equivalencias = daoRotond.darEquivalenciaIngre();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalencias;
	}

	public VOEquivalenciaIngrediente buscarEquivIngrePorId(Integer idEquiv) throws Exception {
		VOEquivalenciaIngrediente equivalencia;
		DAOEquivalenciaIngrediente daoRotond = new DAOEquivalenciaIngrediente();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			equivalencia = (VOEquivalenciaIngrediente) daoRotond.buscarEquivIngrePorID(Long.valueOf(idEquiv));

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalencia;
	}

	public void addEquivalenciaIngre(VOEquivalenciaIngrediente equivalencia) throws Exception {
		DAOEquivalenciaIngrediente daoRotond = new DAOEquivalenciaIngrediente();
		DAOProductoIngrediente daoProdIngre = new DAOProductoIngrediente();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			daoProdIngre.setConn(conn);

			ProductoIngrediente RP2 = daoProdIngre.buscarIngredienteProductoPorNameIngrediente(equivalencia.getIngrediente2().getNombre());	
			System.out.println("RP2: "+RP2.getIngrediente().getNombre());
			ProductoIngrediente RP1 = daoProdIngre.buscarIngredienteProductoPorNameIngrediente(equivalencia.getIngrediente1().getNombre());
			System.out.println("RP1: "+RP1.getIngrediente().getNombre());

			if(RP1.getProducto().getNombre().equals(RP2.getProducto().getNombre())) {
				daoRotond.addEquivIngre(equivalencia);
				conn.commit();
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				daoProdIngre.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	///////Transacciones Resta PRODUCTO//////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////
	/////////////////////////////////////////////////

	public List<ProductoIngrediente> darProdIngre() throws Exception {
		List<ProductoIngrediente> prodIngred;
		DAOProductoIngrediente daoRotond = new DAOProductoIngrediente();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			prodIngred = daoRotond.darProductoIngrediente();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return prodIngred;
	}

	public void addProductoIngrediente(ProductoIngrediente prodIngre) throws Exception {
		DAOProductoIngrediente daoProdIngre = new DAOProductoIngrediente();
		DAOIngredienteRotond daoIngrediente = new DAOIngredienteRotond();
		DAOProductoRotond daoProducto = new DAOProductoRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoProdIngre.setConn(conn);
			daoIngrediente.setConn(conn);
			daoProducto.setConn(conn);
			if(daoProducto.buscarProductoPorName(prodIngre.getProducto().getNombre())!=null&&daoIngrediente.buscarIngredientesPorName(prodIngre.getIngrediente().getNombre())!=null) {
				prodIngre.setProducto(daoProducto.buscarProductoPorName(prodIngre.getProducto().getNombre()).get(0));
				prodIngre.setIngrediente(daoIngrediente.buscarIngredientesPorName(prodIngre.getIngrediente().getNombre()).get(0));
				daoProdIngre.addProductoIngrediente(prodIngre);
				conn.commit();
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProdIngre.cerrarRecursos();
				daoIngrediente.cerrarRecursos();
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//-------------------------------------------
	//-------------------------------------------
	//*** ***** **** ****   *  *
	// *    *   **   * - *  ****    
	//***   *   **** *   \     *
	//-------------------------------------------
	//-------------------------------------------
	public List<VOUsuarioProducto> consultarConsumo(String restaurante,String criterio,String funcion,String fechaInic,String fechaFin) throws SQLException
	{
		System.out.println("ENTRA A CONSULTAR CONSUMO");
		List<VOUsuarioProducto> usuarios;
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			usuarios = daoRotond.consultarConsumo(restaurante, criterio, funcion, fechaInic, fechaFin);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}
	
	public List<Usuario> consultarNoConsumo(String restaurante,String fechaInic,String fechaFin) throws SQLException
	{
		System.out.println("ENTRA A CONSULTAR CONSUMO");
		List<Usuario> usuarios;
		DAOUsuarioRotond daoRotond = new DAOUsuarioRotond();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			usuarios = daoRotond.consultarNoConsumo(restaurante , fechaInic, fechaFin);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}
	public VOConsultaFuncionamiento consultarFuncionamiento() throws SQLException
	{
		
		DAOPedidoProductoRotond daoRotond = new DAOPedidoProductoRotond();
		VOConsultaFuncionamiento vo;
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			daoRotond.setConn(conn);
			vo = daoRotond.consultarFuncionamiento();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotond.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return vo;
	}
	
}
