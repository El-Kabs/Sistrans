package vos;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class ProductoConId {
	
	@JsonProperty(value="id")
	private int id;
	@JsonProperty(value="nombre")
	private String nombre;
	@JsonProperty(value="info")
	private String info;
	@JsonProperty(value="traduccion")
	private String traduccion;
	@JsonProperty(value="preparacion")
	private String preparacion;
	@JsonProperty(value="costoProduccion")
	private double costoProduccion;
	@JsonProperty(value="precio")
	private double precio;
	@JsonProperty(value="categoria")
	private Categoria categoria;
	
	
	public ProductoConId(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="info")String info,@JsonProperty(value="traduccion")String traduccion,@JsonProperty(value="preparacion")String preparacion,@JsonProperty(value="costoProduccion")double costoProduccion,@JsonProperty(value="precio")double precio,@JsonProperty(value="categoria")Categoria categoria)
	{
		this.id = id;
		this.nombre=nombre;
		this.info=info;
		this.traduccion=traduccion;
		this.preparacion=preparacion;
		this.costoProduccion=costoProduccion;
		this.precio=precio;
		this.categoria=categoria;
	}
	
	

	public ProductoConId() {
		// TODO Auto-generated constructor stub
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTraduccion() {
		return traduccion;
	}

	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}

	public String getPreparacion() {
		return preparacion;
	}

	public void setPreparacion(String preparacion) {
		this.preparacion = preparacion;
	}

	public double getCostoProduccion() {
		return costoProduccion;
	}

	public void setCostoProduccion(double costoProduccion) {
		this.costoProduccion = costoProduccion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	
	
	
}
