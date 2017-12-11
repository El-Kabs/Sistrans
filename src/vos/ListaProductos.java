package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ListaProductos {
	@JsonProperty(value="productos")
	private List<ProductoConId> producto;
	
	/**
	 * Constructor de la clase ListaVideos
	 * @param videos - videos para agregar al arreglo de la clase
	 */
	public ListaProductos( @JsonProperty(value="productos")List<ProductoConId> producto){
		this.producto = producto;
	}
	
	public ListaProductos( ArrayList<Producto> producto){
		
		List<ProductoConId> ret = new ArrayList<ProductoConId>();
		for(int i = 0; i<producto.size(); i++){
			ProductoConId agregar = new ProductoConId();
			agregar.setId(0);
			agregar.setNombre(producto.get(i).getNombre());
			agregar.setCategoria(producto.get(i).getCategoria());
			agregar.setCostoProduccion(producto.get(i).getCostoProduccion());
			agregar.setInfo(producto.get(i).getInfo());
			agregar.setPrecio(producto.get(i).getPrecio());
			agregar.setPreparacion(producto.get(i).getPreparacion());
			agregar.setTraduccion(producto.get(i).getTraduccion());
			ret.add(agregar);
		}
		this.producto = ret;
	}

	/**
	 * Método que retorna la lista de videos
	 * @return  List - List con los videos
	 */
	public List<ProductoConId> getProductos() {
		return producto;
	}

	/**
	 * Método que asigna la lista de videos que entra como parametro
	 * @param  videos - List con los videos ha agregar
	 */
	public void setVideo(List<ProductoConId> productos) {
		this.producto = productos;
	}
}
