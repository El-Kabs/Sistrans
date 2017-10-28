package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PedidoProductoConEquivalencia {
	@JsonProperty(value="producto")
	private List<Producto> producto;
	@JsonProperty(value="pedido")
	private Pedido pedido;
	@JsonProperty(value="equivalenciasP")
	private List<Producto> equivalenciasP;
	@JsonProperty(value="equivalenciasI")
	private List<Ingrediente> equivalenciasI;
	
	
	
	public PedidoProductoConEquivalencia(@JsonProperty(value="producto")List<Producto> producto, @JsonProperty(value="pedido")Pedido pedido,
			@JsonProperty(value="equivalenciasP")List<Producto> equivalenciasP, @JsonProperty(value="equivalenciasI")List<Ingrediente> equivalenciasI) {
		super();
		this.producto = producto;
		this.pedido = pedido;
		this.equivalenciasP = equivalenciasP;
		this.equivalenciasI = equivalenciasI;
	}


	public List<Producto> getEquivalenciasP() {
		return equivalenciasP;
	}


	public void setEquivalenciasP(List<Producto> equivalenciasP) {
		this.equivalenciasP = equivalenciasP;
	}


	public List<Ingrediente> getEquivalenciasI() {
		return equivalenciasI;
	}


	public void setEquivalenciasI(List<Ingrediente> equivalenciasI) {
		this.equivalenciasI = equivalenciasI;
	}


	public List<Producto> getProducto() {
		return producto;
	}


	public void setProducto(List<Producto> producto) {
		this.producto = producto;
	}


	public Pedido getPedido() {
		return pedido;
	}


	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
}
