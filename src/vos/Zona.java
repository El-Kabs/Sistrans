package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Zona {

	@JsonProperty(value="nombre")
	private String nombre;
	
	public Zona(@JsonProperty(value="nombre")String nombre)
	{
		this.nombre=nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
