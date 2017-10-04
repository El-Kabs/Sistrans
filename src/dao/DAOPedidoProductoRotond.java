package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import vos.Categoria;
import vos.Pago;
import vos.Pedido;
import vos.PedidoProducto;
import vos.Producto;
import vos.Restaurante;
import vos.RestauranteProducto;

public class DAOPedidoProductoRotond {
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOPedidoProductoRotond() {
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
	public ArrayList<PedidoProducto> darPedidoProducto() throws SQLException, Exception {
		ArrayList<PedidoProducto> pedidoProducto = new ArrayList<PedidoProducto>();
		String sql = "SELECT * FROM(SELECT * FROM PEDIDO a JOIN PEDIDO_PRODUCTO c ON a.ID = c.ID_PEDIDO) e JOIN PRODUCTO f ON e.NOMBRE_PRODUCTO = f.NOMBRE"; 
		ArrayList<Producto> productos = new ArrayList<Producto>();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Date fecha= rs.getDate("FECHA");
			Long idPedido = rs.getLong("ID_PEDIDO");
			Long idUsuario = rs.getLong("ID_USUARIO");
			double costo = rs.getDouble("COSTO_TOTAL");
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			String informacionProducto = rs.getString("INFORMACION");
			String traduccionProducto = rs.getString("TRADUCCION");
			String preparacionProducto = rs.getString("PREPARACION");
			int costoProducto = rs.getInt("COSTO_PRODUCCION");
			double precioProducto = rs.getDouble("PRECIO");
			Categoria categoriaProducto = Categoria.valueOf(rs.getString("CATEGORIA"));
			Producto producto =new Producto(nombreProducto, informacionProducto, traduccionProducto, preparacionProducto, costoProducto, precioProducto, categoriaProducto);
			Pedido pedido = new Pedido(idPedido, costo, idUsuario, "PENDIENTE",fecha);
			productos.add(producto);
			pedidoProducto.add(new PedidoProducto(productos, pedido));
		}
		return pedidoProducto;
	}

	public void addPedidoProducto(PedidoProducto pedidoProducto) throws SQLException, Exception {
		
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();
		
		for(int i = 0; i<pedidoProducto.getProducto().size(); i++) {
			String sql2 = "INSERT INTO PEDIDO_PRODUCTO VALUES ("+pedidoProducto.getPedido().getId()+", '"+pedidoProducto.getProducto().get(i).getNombre()+"')";
			PreparedStatement prepStmt = conn.prepareStatement(sql2);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	
	}
	
	public PedidoProducto buscarPedidoProductoPorId(Long id) throws SQLException, Exception 
	{
		PedidoProducto pedidoProducto = null;
		
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();

		String sql = "SELECT * FROM PEDIDO_PRODUCTO WHERE ID_PEDIDO =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long idPedido = rs.getLong("ID_PEDIDO");
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			Pedido pedido = pedidoDao.buscarPedidoPorId(idPedido);
			ArrayList<Producto> producto = productoDAO.buscarProductoPorName(nombreProducto);
			pedidoProducto = new PedidoProducto(producto, pedido);
		}
		return pedidoProducto;
	}
	
	public PedidoProducto buscarPedidoProductoPorName(String name) throws SQLException, Exception 
	{
		PedidoProducto pedidoProducto = null;
		
		DAOPedidoRotond pedidoDao = new DAOPedidoRotond();
		DAOProductoRotond productoDAO = new DAOProductoRotond();

		String sql = "SELECT * FROM PEDIDO_PRODUCTO WHERE NOMBRE_PRODUCTO ='" + name+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long idPedido = rs.getLong("ID_PEDIDO");
			String nombreProducto = rs.getString("NOMBRE_PRODUCTO");
			Pedido pedido = pedidoDao.buscarPedidoPorId(idPedido);
			ArrayList<Producto> producto = productoDAO.buscarProductoPorName(nombreProducto);
			pedidoProducto = new PedidoProducto(producto, pedido);
		}
		return pedidoProducto;
	}
}
