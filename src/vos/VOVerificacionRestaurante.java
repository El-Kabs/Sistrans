package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOVerificacionRestaurante {
	
	@JsonProperty(value="restaur")
	private Restaurante restaurante;
	@JsonProperty(value="admin")
	private Usuario admin;
	
	public VOVerificacionRestaurante(@JsonProperty(value="restaurante")Restaurante restaurante,	@JsonProperty(value="admin")Usuario admin)
	{
		this.restaurante=restaurante;
		this.admin=admin;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public Usuario getAdmin() {
		return admin;
	}

	public void setAdmin(Usuario admin) {
		this.admin = admin;
	}
	

}
