package xgf.main;

/**
 * Class to declare View, Model and Controller, passing View and Model to the Controller
 * @author Xavi
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
		
		Model model = new Model();
		View view = new View();

		new Controller(model, view);
	}

}
