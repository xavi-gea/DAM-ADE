package xgf.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class HallOfFame {

	private JFrame frame;
	private JList<String> listScores;

	/**
	 * Create the application.
	 */
	public HallOfFame() {
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Hall of Fame");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("SCORES");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 414, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 414, 214);
		frame.getContentPane().add(scrollPane);
		
		listScores = new JList<String>();
		scrollPane.setViewportView(listScores);
		
		frame.setVisible(true);
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public JList<String> getListScores() {
		return listScores;
	}
}
