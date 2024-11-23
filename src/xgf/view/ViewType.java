package xgf.view;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

public interface ViewType {

	JTextField getTextNewQuery();
    JTable getTableQueryResult();
    JFrame getFrame();
}
