package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOConsultaUsuarioPedidos {
	
	@JsonProperty(value="pedidosMenu")
	private List<PedidoMenu> pedidosMenu;
	@JsonProperty(value="pedidoProducto")
	private List<PedidoProducto> pedidosProducto;
	
	public VOConsultaUsuarioPedidos(@JsonProperty(value="pedidosMenu")List<PedidoMenu>pedidosMenu,	@JsonProperty(value="pedidoProducto")List<PedidoProducto>pedidosProducto)
	{
		this.pedidosMenu=pedidosMenu;
		this.pedidosProducto=pedidosProducto;
	}

	public List<PedidoMenu> getPedidosMenu() {
		return pedidosMenu;
	}

	public void setPedidosMenu(List<PedidoMenu> pedidosMenu) {
		this.pedidosMenu = pedidosMenu;
	}

	public List<PedidoProducto> getPedidosProducto() {
		return pedidosProducto;
	}

	public void setPedidosProducto(List<PedidoProducto> pedidosProducto) {
		this.pedidosProducto = pedidosProducto;
	}
	
	
	

}
