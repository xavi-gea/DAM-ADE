package xgf.exercises;

import java.io.File;
import java.util.Scanner;

public class Exercise105 {

	public static void run(Scanner sc) {
		
		System.out.println("Nombre del directorio a comprobar: ");
		String directory = sc.next();
		
		System.out.println("Tipo de extension a comprobar (sin .): ");
		String extension = sc.next();
		
		File inputDirectory = new File(directory);
		
		String[] directoryFiles = inputDirectory.list((dir, name) -> name.toLowerCase().endsWith(extension));
		
		System.out.println("Archivos en directorio con extensión " + "."+ extension);
		
		for (String element : directoryFiles) {
			
			System.out.println(element);
		}
	}

}
