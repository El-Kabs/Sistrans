package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class RestauranteProducto {
	
	@JsonProperty(value="restaurante")
	private Restaurante restaurante;
	@JsonProperty(value="producto")
	private Producto producto;
	@JsonProperty(value="cantidad")
	private int cantidad;
	@JsonProperty(value="max")
	private int max;
	
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public Restaurante getRestaurante() {
		return restaurante;
	}
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
	
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public RestauranteProducto(@JsonProperty(value="restaurante")Restaurante restaurante,@JsonProperty(value="producto")Producto producto ,@JsonProperty(value="cantidad")int cantidad,@JsonProperty(value="max")int max) {
		super();
		this.restaurante = restaurante;
		this.producto = producto;
		this.cantidad = cantidad;
		this.max = max;
	}
	
}
