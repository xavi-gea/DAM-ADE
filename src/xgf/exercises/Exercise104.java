package xgf.exercises;

import java.io.File;
import java.util.Scanner;

public class Exercise104 {

	public static void run(Scanner sc) {
		
		System.out.println("Nombre del directorio a comprobar: ");
		String directory = sc.next();
		
		File inputDirectory = new File(directory);
		
		if (inputDirectory.exists()) {
			
			System.out.println("El directorio existe");
			
		}else {
			
			System.out.println("El directorio no existe");
		}
		
	}

}
