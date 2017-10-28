package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PedidoMenu {

	@JsonProperty(value="menu")
	private Menu menu ;
	@JsonProperty(value="pedido")
	private Pedido pedido;
	
	
	public PedidoMenu(@JsonProperty(value="menu")Menu menu, @JsonProperty(value="pedido")Pedido pedido) {
		super();
		this.menu = menu;
		this.pedido = pedido;
	}


	public Menu getMenu() {
		return menu;
	}


	public void setMenu(Menu menu) {
		this.menu = menu;
	}


	public Pedido getPedido() {
		return pedido;
	}


	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}


	
}
