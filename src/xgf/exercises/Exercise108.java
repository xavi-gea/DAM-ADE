package xgf.exercises;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Scanner;

public class Exercise108 {

	public static void run(Scanner sc) {
		
		String scrPath = "src/";
		
		System.out.println("Nombre del fichero a copiar: ");
		String fileName = sc.next();
		fileName = scrPath + fileName;
		
		System.out.println("Nombre del fichero nuevo: ");
		String newFileName = sc.next();
		newFileName = scrPath + newFileName;
		
		File inputFile = new File(fileName);
		File outputFile = new File(newFileName);
		
		try {
			
			if (!inputFile.exists() || !inputFile.isFile()) {
				
				throw new FileNotFoundException("No se ha encontrado el fichero " + inputFile);
			}
			
			System.out.println("Se intenta copiar archivo");
			Files.copy(inputFile.toPath(), outputFile.toPath());

			System.out.println("Archivos que hay después de copiar: ");
			String[] directoryFiles = new File("src").list();
			
			for (String file : directoryFiles) {
				System.out.println(file);
			}
			
			System.out.println("Se intenta borrar " + outputFile.getName());
			Files.delete(outputFile.toPath());
			
			System.out.println("Archivos que hay después de borrar: ");
			directoryFiles = new File("src").list();
			
			for (String file : directoryFiles) {
				System.out.println(file);
			}
			
		} catch (Exception e) {
			
			System.out.println("Error: " + e.getMessage());
		}
	}

}
