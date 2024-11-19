package xgf;

import xgf.controller.Controller;
import xgf.model.Model;
import xgf.view.View;

public class Main {

	public static void main(String[] args) {
		
		View view = new View();
		Model model = new Model();
		
		new Controller(view,model);
	}

}
