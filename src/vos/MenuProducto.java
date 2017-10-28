package vos;

public class MenuProducto {
	
	private Menu menu;
	
	private Producto producto;
	
	public MenuProducto(Menu menu,Producto producto)
	{
		this.menu=menu;
		this.producto=producto;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	
}
