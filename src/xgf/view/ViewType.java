package xgf.view;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author Xavi
 * Declares common methods that can be used by both AdminPanel and ClientPanel
 */
public interface ViewType {

	JTextField getTextNewQuery();
    JTable getTableQueryResult();
    JFrame getFrame();
}
