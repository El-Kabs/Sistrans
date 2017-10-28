package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOEquivalenciaProducto {
	@JsonProperty(value="producto1")
	private Producto producto1;
	@JsonProperty(value="producto2")
	private Producto producto2;
	@JsonProperty(value="id")
	private Long id;
	public Producto getProducto1() {
		return producto1;
	}
	public void setProducto1(Producto producto1) {
		this.producto1 = producto1;
	}
	public Producto getProducto2() {
		return producto2;
	}
	public void setProducto2(Producto producto2) {
		this.producto2 = producto2;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public VOEquivalenciaProducto(@JsonProperty(value="producto1")Producto producto1, @JsonProperty(value="producto2")Producto producto2, @JsonProperty(value="id")Long id) {
		super();
		this.producto1 = producto1;
		this.producto2 = producto2;
		this.id = id;
	}
}
