package xgf.exercises;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;
import java.util.Scanner;

public class Exercise109 {

	public static void run(Scanner sc) {
		
		String workSpace;
		
		do {
			
			System.out.println("Directorio de trabajo: ");
			workSpace = sc.next();
			
		} while (workSpace.equals(null));
		
		workSpace = "src/" + workSpace + "/";
		
		int option;
		
		boolean exitExercise = false;
		
		do {
			
			System.out.println("1. Mostrar información de fichero/directorio");
			System.out.println("2. Crear una carpeta en directorio local.");
			System.out.println("3. Crear un fichero en la carpeta creada.");
			System.out.println("4. Eliminar ficheros/carpetas.");
			System.out.println("5. Renombrar ficheros/carpetas.");
			System.out.println("0. Salir");
			
			System.out.print("Escribe un número por pantalla: ");
			option = sc.nextInt();
			
			switch (option) {
			
				case 1: getInformacion(sc, workSpace); break;
				case 2: creaCarpeta(sc, workSpace); break;
				case 3: creaFichero(sc, workSpace); break;
				case 4: elimina(sc, workSpace); break;
				case 5: renombra(sc, workSpace); break;

				default: exitExercise = true;
			}
			
		} while (exitExercise == false);
		
	}

	private static void renombra(Scanner sc, String workSpace) {
		
		System.out.println("Nombre del fichero o carpeta a renombrar: ");
		String targetElementName = workSpace + sc.next();
		
		System.out.println("Nuevo nombre del fichero o carpeta: ");
		String targetElementNewName = workSpace + sc.next();
		
		try {
			
			File targetElement = new File(targetElementName);
			File newTargetElement = new File(targetElementNewName);
			
			if (!targetElement.exists()) {
				
				throw new FileNotFoundException("El archivo/directorio no existe");
			}
			
			targetElement.renameTo(newTargetElement);
			
			if (newTargetElement.exists()) {
				
				System.out.println("Archivo/Directorio renombrado");
			}
			
		} catch (Exception e) {
			
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void elimina(Scanner sc, String workSpace) {
	
		System.out.println("Nombre del fichero o carpeta a eliminar: ");
		String targetElementName = workSpace + sc.next();
		
		try {
			
			File newElement = new File(targetElementName);
			
			if (!newElement.exists()) {
				
				throw new FileNotFoundException("El archivo/directorio no existe");
			}
			
			newElement.delete();
			
			if (!newElement.exists()) {
				
				System.out.println("Archivo/Directorio eliminado");
			}
			
		} catch (Exception e) {
			
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void creaFichero(Scanner sc, String workSpace) {
		
		System.out.println("Nombre del fichero a crear: ");
		String fileName = workSpace + sc.next();
		
		try {
			
			File newFile = new File(fileName);
			
			if (newFile.exists()) {
				
				throw new FileAlreadyExistsException("El archivo ya existe");
			}
			
			newFile.createNewFile();
			
			if (newFile.exists()) {
				
				System.out.println("Archivo creado");
			}
			
		} catch (Exception e) {
			
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void creaCarpeta(Scanner sc, String workSpace) {
		
		System.out.println("Nombre de la carpeta a crear: ");
		String folderName = workSpace + sc.next();
		
		try {
			
			File newDirectory = new File(folderName);
			
			if (newDirectory.exists()) {
				
				throw new FileAlreadyExistsException("La carpeta ya existe");
			}
			
			newDirectory.mkdir();
			
			if (newDirectory.exists()) {
				
				System.out.println("Carpeta creada");
			}
			
		} catch (Exception e) {
			
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void getInformacion(Scanner sc, String workSpace) {
		
		System.out.println("Nombre del fichero/directorio del que mostrar información: ");
		String targetElement = workSpace + sc.next();
		
		try {
			
			File targetElementFile = new File(targetElement);
			
			if (!targetElementFile.exists()) {
				
				throw new FileNotFoundException("Error: no se ha encontrado el archivo/directorio");
			}
			
			System.out.println("Nombre: " + targetElementFile.getName());
			System.out.println("Directorio: " + targetElementFile.getAbsolutePath());
			System.out.println("Fecha de última modificación: " + new Date(targetElementFile.lastModified()));
			System.out.println("Oculto: " + targetElementFile.isHidden());
			
			System.out.print("Tipo: ");
			
			if (targetElementFile.isFile()) {
				
				System.out.println("Archivo");
				System.out.println("Tamaño: " + targetElementFile.length());
			
			}else {
				
				System.out.println("Directorio");
				String[] directoryFiles = targetElementFile.list();
				
				System.out.println("Número de archivos y ficheros dentro del directorio: " + directoryFiles.length);
				System.out.println("Espacio libre: " + targetElementFile.getFreeSpace());
				System.out.println("Espacio disponible: " + targetElementFile.getUsableSpace());
				System.out.println("Espacio total: " + targetElementFile.getTotalSpace());
			}
			
		} catch (NullPointerException e) {
			
			System.out.println("Error: No se ha especificado un nombre de archivo/directorio");
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		}		
	}

}
