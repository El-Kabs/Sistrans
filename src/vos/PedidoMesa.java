package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PedidoMesa {
	
	@JsonProperty(value="pedidos")
	private List<PedidoProducto> pedidos;
	
	public PedidoMesa(	@JsonProperty(value="pedidos")List<PedidoProducto>pedidos)
	{
		this.pedidos= pedidos;
	}

	public List<PedidoProducto> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<PedidoProducto> pedidos) {
		this.pedidos = pedidos;
	}
}
