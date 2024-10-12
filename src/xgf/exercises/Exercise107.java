package xgf.exercises;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exercise107 {

	public static void run(Scanner sc) {
		
		System.out.println("Nombre del directorio a comprobar: ");
		String directory = sc.next();
		
		File inputDirectory = new File(directory);
		
		String extension = "parar";
		List<String> extensiones = new ArrayList<String>();
		
		System.out.println("Tipo de extensiones a comprobar. Para dejar de poner, introducir 'parar' sin las comillas: ");
		
		do {
			
			extension = sc.next();
			
			if (!extension.equals("parar")) {
				
				extensiones.add(extension);
			}
			
		} while (!extension.equals("parar"));
		
		String[] directoryFiles;
		
		System.out.println("Archivos en directorio con las extensiones indicadas: ");
		
		for (String extensionLista : extensiones) {
			
			directoryFiles = inputDirectory.list((dir, name) -> name.toLowerCase().endsWith(extensionLista));
			
			for (String file : directoryFiles) {
				
				System.out.println(file);
			}
		}
	}
}
