package xgf.exercises;

import java.io.File;
import java.util.Scanner;

public class Exercise106 {

	public static void run(Scanner sc) {

		System.out.println("Nombre del directorio a comprobar: ");
		String directory = sc.next();
		
		System.out.println("Tipo de extensión a comprobar (sin punto). Si no hay extensión poner 'ninguna' sin las comillas: ");
		String extension = sc.next();
		
		File inputDirectory = new File(directory);
		
		String[] directoryFiles;
		
		if (!extension.equals("ninguna")) {
			
			directoryFiles = inputDirectory.list((dir, name) -> name.toLowerCase().endsWith(extension));
			
			System.out.println("Archivos en directorio con extensión " + "."+ extension);
			
		}else {
			
			directoryFiles = inputDirectory.list();
			System.out.println("Archivos en directorio");
		}
		
		for (String element : directoryFiles) {
			
			System.out.println(element);
		}
	}

}
