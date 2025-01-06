package xgf.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xgf.model.*;
import xgf.view.*;

public class Controller {

	private MainPanel viewMain;
	
	public Controller() {
		
		Database.connectToDatabase();
		
		viewMain = new MainPanel();
		initMainEventHandler();
	}
	
	private void initMainEventHandler() {
		
		viewMain.getBtnLogin().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
	}
}
