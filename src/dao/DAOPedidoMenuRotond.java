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
import vos.PedidoMenu;
import vos.PedidoProducto;
import vos.Producto;
import vos.Restaurante;
import vos.RestauranteProducto;
import vos.Menu;

public class DAOPedidoMenuRotond {
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOPedidoMenuRotond() {
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

	public void addPedidoMenu(PedidoMenu pedidoMenu) throws SQLException, Exception {

		String sql="INSERT INTO PEDIDO_MENU VALUES"+pedidoMenu.getPedido().getId()+","+pedidoMenu.getMenu().getId();
		PreparedStatement prpstmt=conn.prepareStatement(sql);
		prpstmt.executeQuery();
		String sql2="UPDATE PEDIDO SET COSTO_TOTAL=COSTO_TOTAL"+pedidoMenu.getMenu().getCosto()+"WHERE ID="+pedidoMenu.getPedido().getId();
		PreparedStatement prpStmt2=conn.prepareStatement(sql2);
		prpStmt2.executeQuery();
	}
	public void deletePedidoMenu(PedidoMenu pedido) throws Exception
	{
		String sql1="SELECT * FROM PEDIDO_MENU WHERE ID_PEDIDO="+pedido.getPedido().getId();
		PreparedStatement preparedStatement=conn.prepareStatement(sql1);
		ResultSet rs=preparedStatement.executeQuery();
		while(rs.next())
		{
			if(rs.getString("ESTADO").equals("ENTREGADO"))
			{
				throw new Exception("El pedido ya fue entregado");
			}
		}
		String sql="DELETE FROM PEDIDO_MENU WHERE ID_PEDIDO="+pedido.getPedido().getId();
		PreparedStatement prpStmt= conn.prepareStatement(sql);
		prpStmt.executeQuery();
		String sql2="DELETE FROM PEDIDO WHERE ID="+pedido.getPedido().getId();
		PreparedStatement prpStmt2=conn.prepareStatement(sql2);
		prpStmt2.executeQuery();
	}
	public ArrayList<PedidoMenu> getPedidosMenuUsuario(Long id) throws SQLException
	{
		String sql="SELECT * FROM(SELECT * FROM PEDIDO_MENU JOIN PEDIDO ON ID_PEDIDO=ID)a1 JOIN MENU ON a1.ID_MENU=MENU.ID\r\n" + 
				"WHERE ID_USUARIO="+id;
		PreparedStatement prpStmt= conn.prepareCall(sql);
		ResultSet rs=prpStmt.executeQuery();
		ArrayList<PedidoMenu>pedidos= new ArrayList<>();
		while(rs.next())
		{
			Long idPedido=rs.getLong("ID_PEDIDO");
			double costoTotal=rs.getDouble("COSTO_TOTAL");
			Long idUsuario=rs.getLong("ID_USUARIO");
			String estado=rs.getString("ESTADO");
			Date fecha=rs.getDate("FECHA");
			String nombre=rs.getString("NOMBRE");
			double costo=rs.getDouble("COSTO_TOTAL_1");
			String restaurante=rs.getString("NOMBRE_RESTAURANTE");
			Pedido pedido= new Pedido(id, costoTotal, idUsuario, estado, fecha);
			Menu menu= new Menu(idUsuario, nombre, costo, restaurante);
			PedidoMenu pedidoMenu= new PedidoMenu(menu, pedido);
			pedidos.add(pedidoMenu);
		}
		return pedidos;
	}
}
