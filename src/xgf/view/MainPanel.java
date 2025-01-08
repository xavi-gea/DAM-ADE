package xgf.view;



import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

public class MainPanel {

	private JFrame frame;
	private JButton btnLoadCards;
	private JButton btnRegister;
	private JButton btnLogin;
	
	JComboBox<String> cbbCardsSuit;
	private JButton btnStart;
	
	private JButton btnSave;
	private JButton btnLogout;
	
	private JButton btnCrupier;
	private JLabel lblTotalScoreCrupier;
	private JLabel lblScoreHistoryCrupier;
	
	private JButton btnPlayer;
	private JLabel lblTotalScorePlayer;
	private JLabel lblScoreHistoryPlayer;
	
	private JButton btnNewCard;
	private JButton btnStand;
	

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
		
		btnLoadCards = new JButton("Load Cards");
		btnLoadCards.setBounds(0, 0, 100, 23);
		panel_3.add(btnLoadCards);
		
		btnRegister = new JButton("Register");
		btnRegister.setBounds(110, 0, 89, 23);
		panel_3.add(btnRegister);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(209, 0, 89, 23);
		panel_3.add(btnLogin);
		
		cbbCardsSuit = new JComboBox<String>();
		cbbCardsSuit.setBounds(393, 0, 44, 22);
		panel_3.add(cbbCardsSuit);
		cbbCardsSuit.setModel(new DefaultComboBoxModel<String>(new String[] {"ES", "FR"}));
		
		JLabel lblCardsSuit = new JLabel("Cards Suit:");
		lblCardsSuit.setBounds(308, 4, 75, 14);
		panel_3.add(lblCardsSuit);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(447, 0, 89, 23);
		panel_3.add(btnStart);
		
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		btnSave.setBounds(546, 0, 89, 23);
		panel_3.add(btnSave);
		
		JButton btnHallOfFame = new JButton("Hall of Fame");
		btnHallOfFame.setBounds(645, 0, 117, 23);
		panel_3.add(btnHallOfFame);
		
		btnLogout = new JButton("Logout");
		btnLogout.setBounds(772, 0, 89, 23);
		panel_3.add(btnLogout);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 45, 298, 496);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblCrupier = new JLabel("CRUPIER");
		lblCrupier.setBounds(0, 0, 56, 14);
		panel.add(lblCrupier);
		
		btnCrupier = new JButton("");
		btnCrupier.setBounds(0, 25, 298, 421);
		panel.add(btnCrupier);
		
		JLabel lblTotalScoreCrupierText = new JLabel("TOTAL SCORE:");
		lblTotalScoreCrupierText.setBounds(0, 457, 100, 14);
		panel.add(lblTotalScoreCrupierText);
		
		JLabel lblScoreHistoryCrupierText = new JLabel("Score history:");
		lblScoreHistoryCrupierText.setBounds(0, 482, 100, 14);
		panel.add(lblScoreHistoryCrupierText);
		
		lblTotalScoreCrupier = new JLabel("");
		lblTotalScoreCrupier.setBounds(110, 457, 46, 14);
		panel.add(lblTotalScoreCrupier);
		
		lblScoreHistoryCrupier = new JLabel("");
		lblScoreHistoryCrupier.setBounds(110, 482, 178, 14);
		panel.add(lblScoreHistoryCrupier);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(457, 45, 298, 496);
		frame.getContentPane().add(panel_1);
		
		JLabel lblPlayer = new JLabel("PLAYER");
		lblPlayer.setBounds(0, 0, 46, 14);
		panel_1.add(lblPlayer);
		
		btnPlayer = new JButton("");
		btnPlayer.setBounds(0, 25, 298, 421);
		panel_1.add(btnPlayer);
		
		JLabel lblTotalScorePlayerText = new JLabel("TOTAL SCORE:");
		lblTotalScorePlayerText.setBounds(0, 457, 100, 14);
		panel_1.add(lblTotalScorePlayerText);
		
		JLabel lblScoreHistoryPlayerText = new JLabel("Score history:");
		lblScoreHistoryPlayerText.setBounds(0, 482, 100, 14);
		panel_1.add(lblScoreHistoryPlayerText);
		
		lblTotalScorePlayer = new JLabel("");
		lblTotalScorePlayer.setBounds(110, 457, 46, 14);
		panel_1.add(lblTotalScorePlayer);
		
		lblScoreHistoryPlayer = new JLabel("");
		lblScoreHistoryPlayer.setBounds(110, 482, 178, 14);
		panel_1.add(lblScoreHistoryPlayer);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(457, 552, 298, 33);
		frame.getContentPane().add(panel_2);
		
		btnNewCard = new JButton("New card");
		btnNewCard.setEnabled(false);
		panel_2.add(btnNewCard);
		
		btnStand = new JButton("Stand");
		btnStand.setEnabled(false);
		panel_2.add(btnStand);
		
		frame.setVisible(true);
		
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public JButton getBtnLoadCards() {
		return btnLoadCards;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public JButton getBtnRegister() {
		return btnRegister;
	}
	
	public JComboBox<String> getCbbCardsSuit() {
		return cbbCardsSuit;
	}
	
	public JButton getBtnStart() {
		return btnStart;
	}

	public JButton getBtnLogout() {
		return btnLogout;
	}

	public JButton getBtnCrupier() {
		return btnCrupier;
	}

	public JLabel getLblTotalScoreCrupier() {
		return lblTotalScoreCrupier;
	}

	public JLabel getLblScoreHistoryCrupier() {
		return lblScoreHistoryCrupier;
	}

	public JButton getBtnPlayer() {
		return btnPlayer;
	}

	public JLabel getLblTotalScorePlayer() {
		return lblTotalScorePlayer;
	}

	public JLabel getLblScoreHistoryPlayer() {
		return lblScoreHistoryPlayer;
	}

	public JButton getBtnNewCard() {
		return btnNewCard;
	}

	public JButton getBtnStand() {
		return btnStand;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	
}
