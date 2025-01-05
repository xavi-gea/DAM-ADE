package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Main {

	public static void main(String[] args) {
		
		// Carga la configuracion y crea un session factory
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		
		//configuration.addClass(Libro.class);
		configuration.addFile("./src/main/libro.hbm.xml");
		
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		
		SessionFactory sessionFactory = configuration.buildSessionFactory(registry);
		
		// Abre una nueva session de la session factory
		Session session = sessionFactory.openSession();
		
		//Aquí la/s operacio/nes CRUD (crear, leer, actualizar, borrar)
		showMenu(session);
		
		//Commit de la transacción. Limpieza de identificadores y cierra sesión
		session.clear();
		session.close();
		
		System.out.println("closed");
	}

	private static void showMenu(Session session) {
		
		Scanner sc = new Scanner(System.in);
		
		boolean exitMenu = false;
		int option;
		
		do {
			
			System.out.println("---");
			System.out.println("1. Mostrar todos los títulos de la biblioteca.");
			System.out.println("2. Mostrar la información detallada de un libro a partir de su id.");
			System.out.println("3. Añadir un nuevo libro a la biblioteca.");
			System.out.println("4. Modificar atributos de un libro a partir de su id.");
			System.out.println("5. Borrar un libro a partir de su id.");
			System.out.println("0. Salir");
			System.out.println("---");
			
			System.out.print("Escribe un número de funcionalidad a elegir por pantalla: ");
			option = sc.nextInt();
			
			switch (option) {
			
				case 1: showAllBooks(session); break;
				case 2: showSpecificBook(session, sc); break;
				case 3: addBook(session, sc); break;
				case 4: updateBook(session, sc); break;
				case 5: removeBook(session, sc); break;
				default: exitMenu = true;
			}
			
		} while (exitMenu != true);
		
		sc.close();
	}

	private static void showAllBooks(Session session) {
		
		session.beginTransaction();
			
		List<Libro> bookList = new ArrayList<Libro>();
		bookList = (List<Libro>) session.createQuery("FROM Libro").list();
		
		for (Libro book : bookList) {
			
			System.out.println("Id: " + book.getId() + " | Título: " + book.getTitulo());
		}
		
		session.getTransaction().commit();
		session.clear();
	}

	private static void showSpecificBook(Session session, Scanner sc) {
				
		Integer bookID;
		
		System.out.println("Identificador del libro: ");
		bookID = sc.nextInt();
		
		session.beginTransaction();
		
		Libro book = (Libro) session.get(Libro.class, bookID);
		
		session.getTransaction().commit();
		session.clear();
		
		if (book != null) {
			
			System.out.println(book.toString());
			
		}else {
			
			System.out.println("No se ha encontrado un libro con ese identificador");
		}
	}

	private static void addBook(Session session, Scanner sc) {
		
		sc.nextLine();
		
		System.out.print("Título: ");
		String title = sc.nextLine();
		
		System.out.print("Autor: ");
		String author = sc.nextLine();
		
		System.out.print("Año de nacimiento: ");
		String year_birth = sc.nextLine();
		
		System.out.print("Año de publicación: ");
		Integer year_publication;
		
		try {
			
			year_publication = Integer.parseInt(sc.nextLine());
			
		} catch (NumberFormatException e) {
			
			year_publication = 0;
			e.printStackTrace();
		}
		
		System.out.print("Editorial: ");
		String editorial = sc.nextLine();
		
		System.out.print("Número de páginas: ");
		Integer page_number;
		
		try {
			
			page_number = Integer.parseInt(sc.nextLine());
			
		} catch (NumberFormatException e) {
			
			page_number = 0;
			e.printStackTrace();
		}
		
		Libro book = new Libro(title, author, year_birth, year_publication, editorial, page_number);
		
		session.beginTransaction();
		
		session.save(book);
		
		session.getTransaction().commit();
		
		//Serializable id = session.save(book);
		session.clear();
		
		System.out.println("Libro añadido");
	}

	private static void updateBook(Session session, Scanner sc) {
		
		sc.nextLine();
		
		Integer bookID = 0;
		
		System.out.print("Identificador del libro: ");
		
		try {
			
			bookID = Integer.parseInt(sc.nextLine());
			
		} catch (NumberFormatException e) {
			
			bookID = 0;
			e.printStackTrace();
		}
		
		session.beginTransaction();
		
		Libro book = (Libro) session.load(Libro.class, bookID);
		
		if (book != null) {
			
			System.out.print("Título (actual " + book.getTitulo() + "): ");
			book.setTitulo(sc.nextLine());
			
			System.out.print("Autor (actual " + book.getAutor() + "): ");
			book.setAutor(sc.nextLine());
			
			System.out.print("Año de nacimiento (actual " + book.getAnyo_nacimiento() + "): ");
			book.setAnyo_nacimiento(sc.nextLine());
			
			System.out.print("Año de publicación (actual " + book.getAnyo_publicacion() + "): ");
			Integer year_publication;
			
			try {
				
				year_publication = Integer.parseInt(sc.nextLine());
				
			} catch (NumberFormatException e) {
				
				year_publication = 0;
				e.printStackTrace();
			}
			
			book.setAnyo_publicacion(year_publication);
			
			System.out.print("Editorial (actual " + book.getEditorial() + "): ");
			book.setEditorial(sc.nextLine());
			
			System.out.print("Número de páginas (actual " + book.getNumero_paginas() + "): ");
			Integer page_number;
			
			try {
				
				page_number = Integer.parseInt(sc.nextLine());
				
			} catch (NumberFormatException e) {
				
				page_number = 0;
				e.printStackTrace();
			}
			
			book.setNumero_paginas(page_number);
			
			session.update(book);
			
			System.out.println("Libro Modificado");
			
		}else {
			
			System.out.println("No se ha encontrado un libro con ese identificador");
		}
		
		session.getTransaction().commit();
		
		//Serializable id = session.save(book);
		session.clear();
		
	}

	private static void removeBook(Session session, Scanner sc) {
		
		Integer bookID;
		
		System.out.println("Identificador del libro: ");
		bookID = sc.nextInt();
		
		session.beginTransaction();
		
		Libro book = (Libro) session.get(Libro.class, bookID);
		
		if (book != null) {
			
			session.delete(book);
			
			System.out.println("Libro eliminado");
			
		}else {
			
			System.out.println("No se ha encontrado un libro con ese identificador");
		}
		
		session.getTransaction().commit();
		session.clear();
	}
}
