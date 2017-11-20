package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOUsuarioProducto {
	
	@JsonProperty(value="usuario")
	private Usuario usuario;
	@JsonProperty(value="producto")
	private String producto;
	@JsonProperty(value="categProducto")
	private String categProducto;
	
	public VOUsuarioProducto(@JsonProperty(value="usuario") Usuario usuario,@JsonProperty(value="producto") String producto,@JsonProperty(value="categProducto") String categProducto)
	{
		this.usuario= usuario;
		this.producto= producto;
		this.categProducto=categProducto;
	}

}
