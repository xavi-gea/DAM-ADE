package xgf.main;

import java.util.Scanner;

import xgf.exercises.Exercise101;
import xgf.exercises.Exercise102;
import xgf.exercises.Exercise103;
import xgf.exercises.Exercise104;
import xgf.exercises.Exercise105;
import xgf.exercises.Exercise106;
import xgf.exercises.Exercise107;
import xgf.exercises.Exercise108;
import xgf.exercises.Exercise109;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
				
		int option;
		
		boolean exitApp = false;
		
		do {
			
			System.out.println("---");
			System.out.println("101. Realiza un programa que reciba como parámetro de entrada un directorio y lo muestre por pantalla.");
			System.out.println("102. Ampliar el programa anterior para que muestre todas las características de interés del directorio, tomando como referencia la información que proporciona la clase File");
			System.out.println("103. Introducir una comprobación en el programa anterior para determinar si el directorio existe.");
			System.out.println("104. Realizar un programa que, dado un directorio, compruebe si existe y devuelva un mensaje de confirmación si existe o una alerta en caso contrario.");
			System.out.println("105. Realiza un programa que reciba como parámetros de entrada un directorio y una extensión de fichero (por ejemplo .txt) y devuelva por pantalla todos los ficheros del directorio que cumplan el criterio.");
			System.out.println("106. Modifica el programa anterior para que tenga en cuenta que, si no se le pasa ninguna extensión como parámetro, muestre todo el contenido del directorio.");
			System.out.println("107. Modifica el programa anterior para que admita como parámetros de entrada un nombre cualquiera de extensiones, devolviendo después por pantalla todos los ficheros del directorio que tengan alguna de las extensiones indicadas.");
			System.out.println("108. Desarrolla un programa que, dado un fichero, realice una copia del mismo (en el mismo directorio y cambiándole el nombre) y lo borre después. Muestra una traza por pantalla de las acciones para ver que se realizan.");
			System.out.println("109. Miniproyecto");
			System.out.println("0. Salir");
			System.out.println("---");
			
			System.out.print("Escribe un número de ejercicio a elegir por pantalla: ");
			option = sc.nextInt();
			
			switch (option) {
			
				case 101: Exercise101.run(sc); break;
				case 102: Exercise102.run(sc); break;
				case 103: Exercise103.run(sc); break;
				case 104: Exercise104.run(sc); break;
				case 105: Exercise105.run(sc); break;
				case 106: Exercise106.run(sc); break;
				case 107: Exercise107.run(sc); break;
				case 108: Exercise108.run(sc); break;
				case 109: Exercise109.run(sc); break;
				default: exitApp = true;
			}
			
		} while (exitApp != true);
		
		System.out.println("Se sale del programa");
		
		sc.close();
	}
}
