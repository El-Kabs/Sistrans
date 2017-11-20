package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOMaxRestaurante {
	@JsonProperty(value="numeroPedidos")
	private int numeroPedidos;
	@JsonProperty(value="restaurante")	
	private String restaurante;
	
	public VOMaxRestaurante(@JsonProperty(value="numeroPedidos")int numeroPedidos,	@JsonProperty(value="restuarnte")String restaurante)
	{
		this.numeroPedidos=numeroPedidos;
		this.restaurante=restaurante;
	}

	public int getNumeroPedidos() {
		return numeroPedidos;
	}

	public void setNumeroPedidos(int numeroPedidos) {
		this.numeroPedidos = numeroPedidos;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	
}
