package main;

public class Libro {
	
	private Integer id;
	private String titulo;
	private String autor;
	private String anyo_nacimiento;
	private Integer anyo_publicacion;
	private String editorial;
	private Integer numero_paginas;
	
	public Libro() {
		super();
	}

	public Libro(Integer id, String titulo, String autor, String anyo_nacimiento, Integer anyo_publicacion, String editorial, Integer numero_paginas) {
		
		super();
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.anyo_nacimiento = anyo_nacimiento;
		this.anyo_publicacion = anyo_publicacion;
		this.editorial = editorial;
		this.numero_paginas = numero_paginas;
	}
	
	public Libro(String titulo, String autor, String anyo_nacimiento, Integer anyo_publicacion, String editorial, Integer numero_paginas) {
		
		super();
		this.titulo = titulo;
		this.autor = autor;
		this.anyo_nacimiento = anyo_nacimiento;
		this.anyo_publicacion = anyo_publicacion;
		this.editorial = editorial;
		this.numero_paginas = numero_paginas;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getAnyo_nacimiento() {
		return anyo_nacimiento;
	}

	public void setAnyo_nacimiento(String anyo_nacimiento) {
		this.anyo_nacimiento = anyo_nacimiento;
	}

	public Integer getAnyo_publicacion() {
		return anyo_publicacion;
	}

	public void setAnyo_publicacion(Integer anyo_publicacion) {
		this.anyo_publicacion = anyo_publicacion;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public Integer getNumero_paginas() {
		return numero_paginas;
	}

	public void setNumero_paginas(Integer numero_paginas) {
		this.numero_paginas = numero_paginas;
	}

	@Override
	public String toString() {
		return "Libro [id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", anyo_nacimiento=" + anyo_nacimiento
				+ ", anyo_publicacion=" + anyo_publicacion + ", editorial=" + editorial + ", numero_paginas="
				+ numero_paginas + "]";
	}
}
