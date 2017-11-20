package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VODiaRestaurante {
	@JsonProperty(value="maximo")
	private VOMaxRestaurante maximo;
	@JsonProperty(value="minimo")
	private VOMinRestaurante minimo;
	@JsonProperty(value="dia")
	private String dia;

	public VODiaRestaurante(@JsonProperty(value="maximo")VOMaxRestaurante maximo,	@JsonProperty(value="minimo")VOMinRestaurante minimo,	@JsonProperty(value="dia")String dia)
	{
		this.minimo=minimo;
		this.maximo=maximo;
		this.dia=dia;
	}
	

	public VOMaxRestaurante getMaximo() {
		return maximo;
	}


	public void setMaximo(VOMaxRestaurante maximo) {
		this.maximo = maximo;
	}


	public VOMinRestaurante getMinimo() {
		return minimo;
	}


	public void setMinimo(VOMinRestaurante minimo) {
		this.minimo = minimo;
	}


	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}
}
