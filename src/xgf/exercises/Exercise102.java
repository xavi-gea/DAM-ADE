package xgf.exercises;

import java.io.File;
import java.util.Scanner;

public class Exercise102 {

	public static void run(Scanner sc) {
		
		System.out.println("Nombre del directorio a analizar: ");
		String directory = sc.next();
		
		File inputDirectory = new File(directory);
		System.out.println("Existe: " + inputDirectory.exists());
		System.out.println("Se puede leer: " + inputDirectory.canRead());
		System.out.println("Se puede escribir: " + inputDirectory.canWrite());
		System.out.println("Nombre: " + inputDirectory.getName());
		System.out.println("Ruta absoluta: " + inputDirectory.getAbsolutePath());
		System.out.println("Ruta: " + inputDirectory.getPath());
		
		String[] directoryFiles = inputDirectory.list();
		System.out.println("Archivos y ficheros dentro del directorio: ");
		
		for (String file : directoryFiles) {
			System.out.println(file);
		}
		
		System.out.println("Carpeta padre: " + inputDirectory.getParent());
	}

}
