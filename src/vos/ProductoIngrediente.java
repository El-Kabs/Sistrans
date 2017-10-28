package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductoIngrediente {
	@JsonProperty(value="producto")
	private Producto producto;
	@JsonProperty(value="ingrediente")
	private Ingrediente ingrediente;
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Ingrediente getIngrediente() {
		return ingrediente;
	}
	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}
	public ProductoIngrediente(@JsonProperty(value="producto")Producto producto,@JsonProperty(value="ingrediente")Ingrediente ingrediente) {
		super();
		this.ingrediente = ingrediente;
		this.producto = producto;
	}
}
