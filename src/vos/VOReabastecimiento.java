package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOReabastecimiento {
	
	@JsonProperty(value="usuarioRestaurante")
	private Usuario restaurante;
	@JsonProperty(value="restuaranteProducto")
	private RestauranteProducto restauranteProducto;
	
	public VOReabastecimiento(@JsonProperty(value="restuaranteProducto")RestauranteProducto restauranteProducto,@JsonProperty(value="usuarioRestaurante")Usuario usuario)
	{
		this.restaurante=usuario;
		this.restauranteProducto=restauranteProducto;
	}

	public Usuario getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Usuario restaurante) {
		this.restaurante = restaurante;
	}

	public RestauranteProducto getRestauranteProducto() {
		return restauranteProducto;
	}

	public void setRestauranteProducto(RestauranteProducto restauranteProducto) {
		this.restauranteProducto = restauranteProducto;
	}
	
	
	

}
