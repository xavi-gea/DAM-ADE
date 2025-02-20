package model;

public class Film {

	private Integer identifier;
	private String titulo;
	private String director;
	private String genero;
	private String base64;
	
	public Film() {
		super();
	}

	public Film(Integer identifier, String titulo, String director, String genero, String base64) {
		
		super();
		this.identifier = identifier;
		this.titulo = titulo;
		this.director = director;
		this.genero = genero;
		this.base64 = base64;
	}
	
	public Film(String titulo, String director, String genero, String base64) {
		
		super();
		this.titulo = titulo;
		this.director = director;
		this.genero = genero;
		this.base64 = base64;
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDirector() {
		return director;
	}

	public String getGenero() {
		return genero;
	}

	public String getBase64() {
		return base64;
	}

	@Override
	public String toString() {
		return "Film [identifier=" + identifier + ", titulo=" + titulo + ", director=" + director + ", genero=" + genero
				+ ", base64=" + base64 + "]";
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
}
