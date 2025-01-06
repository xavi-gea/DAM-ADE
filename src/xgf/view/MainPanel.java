package xgf.view;



import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

public class MainPanel {

	private JFrame frame;
	private JButton btnLogin;
	

	/**
	 * Create the application.
	 */
	public MainPanel() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 896, 648);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 11, 861, 23);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JButton btnLoadCards = new JButton("Load Cards");
		btnLoadCards.setBounds(0, 0, 100, 23);
		panel_3.add(btnLoadCards);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(110, 0, 89, 23);
		panel_3.add(btnRegister);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(209, 0, 89, 23);
		panel_3.add(btnLogin);
		
		JComboBox<String> cbbCardsSuit = new JComboBox<String>();
		cbbCardsSuit.setBounds(393, 0, 44, 22);
		panel_3.add(cbbCardsSuit);
		cbbCardsSuit.setModel(new DefaultComboBoxModel<String>(new String[] {"ES", "FR"}));
		
		JLabel lblCardsSuit = new JLabel("Cards Suit:");
		lblCardsSuit.setBounds(308, 4, 75, 14);
		panel_3.add(lblCardsSuit);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(447, 0, 89, 23);
		panel_3.add(btnStart);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(546, 0, 89, 23);
		panel_3.add(btnSave);
		
		JButton btnHallOfFame = new JButton("Hall of Fame");
		btnHallOfFame.setBounds(645, 0, 117, 23);
		panel_3.add(btnHallOfFame);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(772, 0, 89, 23);
		panel_3.add(btnLogout);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 45, 298, 496);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblCrupier = new JLabel("CRUPIER");
		lblCrupier.setBounds(0, 0, 56, 14);
		panel.add(lblCrupier);
		
		JButton btnCrupier = new JButton("");
		btnCrupier.setEnabled(false);
		btnCrupier.setBounds(0, 25, 298, 421);
		panel.add(btnCrupier);
		
		JLabel lblTotalScoreCrupierText = new JLabel("TOTAL SCORE:");
		lblTotalScoreCrupierText.setBounds(0, 457, 100, 14);
		panel.add(lblTotalScoreCrupierText);
		
		JLabel lblScoreHistoryCrupierText = new JLabel("Score history:");
		lblScoreHistoryCrupierText.setBounds(0, 482, 100, 14);
		panel.add(lblScoreHistoryCrupierText);
		
		JLabel lblTotalScoreCrupier = new JLabel("");
		lblTotalScoreCrupier.setBounds(110, 457, 46, 14);
		panel.add(lblTotalScoreCrupier);
		
		JLabel lblScoreHistoryCrupier = new JLabel("");
		lblScoreHistoryCrupier.setBounds(110, 482, 89, 14);
		panel.add(lblScoreHistoryCrupier);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(457, 45, 298, 496);
		frame.getContentPane().add(panel_1);
		
		JLabel lblPlayer = new JLabel("PLAYER");
		lblPlayer.setBounds(0, 0, 46, 14);
		panel_1.add(lblPlayer);
		
		JButton btnPlayer = new JButton("");
		btnPlayer.setEnabled(false);
		btnPlayer.setBounds(0, 25, 298, 421);
		panel_1.add(btnPlayer);
		
		JLabel lblTotalScorePlayerText = new JLabel("TOTAL SCORE:");
		lblTotalScorePlayerText.setBounds(0, 457, 100, 14);
		panel_1.add(lblTotalScorePlayerText);
		
		JLabel lblScoreHistoryPlayerText = new JLabel("Score history:");
		lblScoreHistoryPlayerText.setBounds(0, 482, 100, 14);
		panel_1.add(lblScoreHistoryPlayerText);
		
		JLabel lblTotalScorePlayer = new JLabel("");
		lblTotalScorePlayer.setBounds(110, 457, 46, 14);
		panel_1.add(lblTotalScorePlayer);
		
		JLabel lblScoreHistoryPlayer = new JLabel("");
		lblScoreHistoryPlayer.setBounds(110, 482, 89, 14);
		panel_1.add(lblScoreHistoryPlayer);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(457, 552, 298, 33);
		frame.getContentPane().add(panel_2);
		
		JButton btnNewCard = new JButton("New card");
		panel_2.add(btnNewCard);
		
		JButton btnStand = new JButton("Stand");
		panel_2.add(btnStand);
		
		frame.setVisible(true);
		
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}
}
