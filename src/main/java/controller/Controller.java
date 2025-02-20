package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import model.*;

public class Controller {

	public Controller() {
		
		new Config();
		Database.setConnectionString();
		
		showMenu();
	}
	
	private static void showMenu() {
		
		Scanner sc = new Scanner(System.in);
		
		boolean exitMenu = false;
		int option;
		
		do {
			
			System.out.println("---");
			System.out.println("1. Mostrar todos los títulos del videoclub.");
			System.out.println("2. Mostrar la información detallada de una peli a partir de su id.");
			System.out.println("3. Añadir una nueva peli al videoclub.");
			System.out.println("4. Modificar atributos de una peli a partir de su id.");
			System.out.println("5. Borrar una peli a partir de su id.");
			System.out.println("0. Salir");
			System.out.println("---");
			
			System.out.print("Escribe un número de funcionalidad a elegir por pantalla: ");
			option = sc.nextInt();
			
			switch (option) {
			
				case 1: showAllFilms(); break;
				case 2: showSpecificFilm(sc); break;
				case 3: addFilm(sc); break;
				case 4: updateFilm(sc); break;
				case 5: removeFilm(sc); break;
				default: exitMenu = true;
			}
			
		} while (exitMenu != true);
		
		sc.close();
	}

	private static void showAllFilms() {
		
		List<Film> filmList = Database.getFilmsFromCollection(Config.getCollections().getString("peliculas"));
		
		for (Film film : filmList) {
			
			System.out.println("Id: " + film.getIdentifier() + " | Título: " + film.getTitulo());
		}
	}
	
	private static void showSpecificFilm(Scanner sc) {
		
		Integer filmID;
		
		System.out.println("Identificador de la pelicula: ");
		filmID = sc.nextInt();
		
		Film film = Database.getFilmFromCollection(filmID, Config.getCollections().getString("peliculas"));
		
		if (film != null) {
			
			System.out.println(film.toString());
			
		}else {
			
			System.out.println("No se ha encontrado una pelicula con ese identificador");
		}
	}
	
	private static void addFilm(Scanner sc) {
		
		sc.nextLine();
		
		System.out.print("Título: ");
		String title = sc.nextLine();
		
		System.out.print("Director: ");
		String director = sc.nextLine();
		
		System.out.print("Genero: ");
		String gender = sc.nextLine();
		
		System.out.print("Base64: ");
		String base64 = sc.nextLine();
		
		Film film = new Film(title, director, gender, base64);
		
		Database.insertFilmToCollection(film, Config.getCollections().getString("peliculas"));
		
		System.out.println("Pelicula añadida");
	}
	
	
	
	private static void updateFilm(Scanner sc) {
		
		sc.nextLine();
		
		Integer filmID = 0;
		
		System.out.print("Identificador de la pelicula: ");
		
		try {
			
			filmID = Integer.parseInt(sc.nextLine());
			
		} catch (NumberFormatException e) {
			
			filmID = 0;
			e.printStackTrace();
		}
		
		Film film = Database.getFilmFromCollection(filmID, Config.getCollections().getString("peliculas"));
		
		if(film != null) {
			
			System.out.print("Título (actual " + film.getTitulo() + "): ");
			film.setTitulo(sc.nextLine());
			
			System.out.print("Director (actual " + film.getDirector() + "): ");
			film.setDirector(sc.nextLine());
			
			System.out.print("Genero (actual " + film.getGenero() + "): ");
			film.setGenero(sc.nextLine());
			
			System.out.print("Base64 (actual " + film.getBase64() + "): ");
			film.setBase64(sc.nextLine());
			
			if (Database.updateFilmFromCollection(film, Config.getCollections().getString("peliculas"))) {
				
				System.out.println("Pelicula actualizada");
				
			}else {
				
				System.out.println("No se ha actualizado la pelicula");
			}
			
		}else {
			
			System.out.println("No se ha encontrado una pelicula con ese identificador");
		}
	}

	private static void removeFilm(Scanner sc) {
		
		sc.nextLine();
		
		String filmName;
		
		System.out.println("Nombre de la pelicula: ");
		filmName = sc.nextLine();
		
		if (Database.removeFilmFromCollection(filmName, Config.getCollections().getString("peliculas"))) {
			
			System.out.println("Pelicula eliminada");
			
		}else {
			
			System.out.println("No se ha encontrado una pelicula con ese nombre");
		}
	}
}
