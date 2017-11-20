package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOMin {
	
	@JsonProperty(value="cantidad")
	private int cantidad;
	@JsonProperty(value="producto")
	private String producto;
	
	public VOMin(@JsonProperty(value="cantidad")int cantidad,@JsonProperty(value="producto")String producto)
	{
		this.cantidad= cantidad;
		this.producto=producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}
	
	

}
