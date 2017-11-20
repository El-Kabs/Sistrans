package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOConsultaFuncionamiento {
	@JsonProperty(value="productos")
	private List<VODia> productos;
	@JsonProperty(value="restaurantes")
	private List<VODiaRestaurante> restaurantes;
	public VOConsultaFuncionamiento(@JsonProperty(value="productos")List<VODia> productos,@JsonProperty(value="restaurantes")List<VODiaRestaurante>restaurantes)
	{
		this.productos=productos;
		this.restaurantes=restaurantes;
	}
	public List<VODia> getProductos() {
		return productos;
	}
	public void setProductos(List<VODia> productos) {
		this.productos = productos;
	}
	public List<VODiaRestaurante> getRestaurantes() {
		return restaurantes;
	}
	public void setRestaurantes(List<VODiaRestaurante> restaurantes) {
		this.restaurantes = restaurantes;
	}
	
}
