package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class VOEquivalenciaIngrediente {
	@JsonProperty(value="ingrediente1")
	private Ingrediente ingrediente1;
	@JsonProperty(value="ingrediente2")
	private Ingrediente ingrediente2;
	@JsonProperty(value="id")
	private Long id;
	public Ingrediente getIngrediente1() {
		return ingrediente1;
	}
	public void setIngrediente1(Ingrediente ingrediente1) {
		this.ingrediente1 = ingrediente1;
	}
	public Ingrediente getIngrediente2() {
		return ingrediente2;
	}
	public void setIngrediente2(Ingrediente ingrediente2) {
		this.ingrediente2 = ingrediente2;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public VOEquivalenciaIngrediente(@JsonProperty(value="ingrediente1")Ingrediente ingrediente1, @JsonProperty(value="ingrediente2")Ingrediente ingrediente2, @JsonProperty(value="id")Long id) {
		super();
		this.ingrediente1 = ingrediente1;
		this.ingrediente2 = ingrediente2;
		this.id = id;
	}
}
