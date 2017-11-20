package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VODia {
	@JsonProperty(value="maximo")
	private VOMax maximo;
	@JsonProperty(value="minimo")
	private VOMin minimo;
	@JsonProperty(value="dia")
	private String dia;

	public VODia(	@JsonProperty(value="maximo")VOMax maximo,	@JsonProperty(value="minimo")VOMin minimo,	@JsonProperty(value="dia")String dia)
	{
		this.minimo=minimo;
		this.maximo=maximo;
		this.dia=dia;
	}

	public VOMax getMaximo() {
		return maximo;
	}

	public void setMaximo(VOMax maximo) {
		this.maximo = maximo;
	}

	public VOMin getMinimo() {
		return minimo;
	}

	public void setMinimo(VOMin minimo) {
		this.minimo = minimo;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}
	
}
