package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOConsultarConsumo {

	@JsonProperty(value="restaurante")
	private String restaurante;
	@JsonProperty(value="fechaInicio")
	private String fechaInic;
	@JsonProperty(value="fechaFin")	
	private String fechaFin;
	@JsonProperty(value="funcion")	
	private String funcion;
	@JsonProperty(value="criterio")	
	private String criterio;

	public VOConsultarConsumo(@JsonProperty(value="restaurante")String restaurante,@JsonProperty(value="fechaInicio")String fechaInic,@JsonProperty(value="fechaFin")	String fechaFin,@JsonProperty(value="funcion")String funcion,@JsonProperty(value="criterio")String criterio)
	{
		this.restaurante=restaurante;
		this.fechaInic=fechaInic;
		this.fechaFin=fechaFin;
		this.criterio=criterio;
		this.funcion=funcion;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	public String getFechaInic() {
		return fechaInic;
	}

	public void setFechaInic(String fechaInic) {
		this.fechaInic = fechaInic;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}
	
	

}
