package xgf.exercises;

import java.io.File;
import java.util.Scanner;

public class Exercise101 {

	public static void run(Scanner sc) {
		
		System.out.println("Nombre del directorio a seleccionar: ");
		String directory = sc.next();
		
		File inputDirectory = new File(directory);
		System.out.println(inputDirectory.getName());
	}
}
