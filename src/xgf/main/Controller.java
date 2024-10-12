package xgf.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class to handle Model and View with listeners
 * @author Xavi
 * @version 1.0
 */
public class Controller {
	
	private Model model;
	private View view;

	/**
	 * Constructor to assign the model and view variables and initialize the event handlers
	 * @param model
	 * @param view
	 */
	public Controller(Model model, View view) {
		
		this.model = model;
		this.view = view;
		
		initEventHandlers();
	}
	
	/**
	 * Initialize event handlers
	 */
	public void initEventHandlers() {
		
		view.getBtnchooseDirectory().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				model.showDirectoryContent(view.getContentPane());
			}
		});
		
		view.getBtnFindInDirectory().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				model.showMatchInDirectory(view.getContentPane(),view.getTextToFindInDirectory(),view.getChckbxCheckUppercase(),view.getChckbxCheckAccent(),view.getChckbxReplaceText(),view.getTextToReplaceInDirectory());
			}
		});
		
		view.getChckbxReplaceText().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if (view.getChckbxReplaceText().isSelected()) {
					
					view.getTextToReplaceInDirectory().setEnabled(true);
					
				}else {
					
					view.getTextToReplaceInDirectory().setText("");
					view.getTextToReplaceInDirectory().setEnabled(false);
				}
			}
		});
	}
}
