package xgf.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.AccessException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import xgf.model.Database;
import xgf.model.Model;
import xgf.model.User;
import xgf.model.XML;
import xgf.view.AdminPanel;
import xgf.view.Login;
import xgf.view.Register;
import xgf.view.View;

public class Controller {
	
	private View view;
	private Model model;
	
	private Login viewLogin;
	private AdminPanel viewAdmin;
//	private ClientPanel viewClient;
	private Register viewRegister;
	
	Connection connection;
	
	User user;
	
	String userName;
	char[] userPassword;
	char[] userPasswordRepeated;
	
	public Controller(View view, Model model) {
		
		this.view = view;
		this.model = model;
		
		viewLogin = new Login();
		
		initLoginEventHandler();
	}

	private void initLoginEventHandler() {
		
		viewLogin.getBtnLogin().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				userName = viewLogin.getTextUser().getText();
				userPassword = viewLogin.getPasswordUser().getPassword();
				
				user = null;
				
				try {
					
					userLogin();
					
				} catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException e1) {
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: Usuario o contraseña incorrectos: " + e1.getMessage());
				}
				
				viewLogin.getTextUser().setText("");
				viewLogin.getPasswordUser().setText("");
			}

			private void userLogin() throws NoSuchAlgorithmException, SQLException, ClassNotFoundException {
				
				user = new User(userName,userPassword);
				
				Database database = new Database("population");
				
				connection = database.connectToDatabase(user.getName(), user.getPassword());
				
				try {
					
					user.setType(database.getUserType(connection,user.getName()));
					
				} catch (SQLException e1) {
					
					user.setType("client");
				}
				
				//connection.close();
				
				viewLogin.getFrame().setVisible(false);
				
				if (user.getType().equals("admin")) {
					
					viewAdmin = new AdminPanel();
					initAdminEventHandlers();
					
				}else if(user.getType().equals("client")) {
					
//					viewClient = new ClientPanel();
//					initClientEventHandlers();
					
					System.out.println("Pasa a ClientPanel");
				}
			}
		});
	}

	private void initAdminEventHandlers() {
		
		viewAdmin.getBtnNewUser().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				viewRegister = new Register();
				initRegisterEventHandlers();
			}
		});
		
		viewAdmin.getBtnImportCSV().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setFileFilter(new FileNameExtensionFilter("Archivo CSV", "csv"));
				
				if (chooser.showOpenDialog(viewAdmin.getFrame()) == JFileChooser.APPROVE_OPTION) {
					
					File chosenFile = chooser.getSelectedFile();
					
					try {
						
						processCSV(chosenFile);
							
					} catch (IOException | SQLException | ParserConfigurationException | TransformerException e1) {
						
						JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error al procesar CSV: " + e1.getMessage());
					}
				}
			}

			private void processCSV(File chosenFile) throws IOException, ParserConfigurationException,
					TransformerException, SQLException, AccessException {
				
				if (Files.isReadable(chosenFile.toPath())) {
					
					BufferedReader reader = Files.newBufferedReader(chosenFile.toPath(), StandardCharsets.ISO_8859_1);
					
					String[] firstLineColumns = reader.readLine().split(";");
					
					String nextLine = reader.readLine();
					String[] splitLine;
					
					String XMLContent = "";
					
					while (nextLine != null) {
						
						splitLine = nextLine.split(";");
						
						XMLContent = XMLContent.concat(XML.createAndReturnFile(splitLine[0],splitLine,firstLineColumns));
						XMLContent = XMLContent.concat("\n");
						
						nextLine = reader.readLine();
					}
					
					reader.close();
					
					viewAdmin.getTextAreaXMLContent().setText(XMLContent);
					
					insertXMLToTable(firstLineColumns);

				}else {
				
					throw new AccessException("No se puede acceder al archivo CSV");
				}
			}

			private void insertXMLToTable(String[] firstLineColumns) throws SQLException {
				
				try {
					
					Database.removeTable(connection, "population");
					Database.createTable(connection, "population", firstLineColumns);
					
					List<String> fileValues = new ArrayList<String>();
					
					for (File file : XML.getCreatedFiles()) {
						
						fileValues = XML.getAttributesAndValues(file);
						
						Database.insertPopulation(connection, "population", fileValues);
					}
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "CSV Importado");

				} catch (SQLException | ParserConfigurationException | SAXException | IOException e) {
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: No ha sido posible insertar XML en base de datos: " + e.getMessage());
				}
				

			}
		});
		
		
		
		
		
		
		
	}
	
	private void initRegisterEventHandlers() {

		viewRegister.getBtnRegister().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					userName = viewRegister.getTextUser().getText();
					userPassword = viewRegister.getPasswordUser().getPassword();
					userPasswordRepeated = viewRegister.getPasswordUserRepeat().getPassword();
					
					if (!userName.isEmpty() && !userPassword.equals(null) && !userPasswordRepeated.equals(null)) {
						
						user = new User(userName, userPassword);
						
						if (!(user.getPassword().equals(User.getHash(userPasswordRepeated)))) {

							throw new NullPointerException("Las contraseñas no coinciden");
						}
						
						if (connection == null) {

							throw new NullPointerException("No ha podido conectarse con la base de datos");
						}
						
						if (Database.setUpClient(connection, user.getName(), user.getPassword(), "population")) {

							JOptionPane.showMessageDialog(viewRegister.getFrame(), "Usuario creado");

							viewRegister.getTextUser().setText("");
							viewRegister.getPasswordUser().setText("");
							viewRegister.getPasswordUserRepeat().setText("");

							viewRegister.getFrame().setVisible(false);
						} 
						
					}else {
						
						throw new NullPointerException("No pueden haber campos vacíos");
					}
					
				} catch (NullPointerException | NoSuchAlgorithmException e2) {
					
					JOptionPane.showMessageDialog(viewRegister.getFrame(), "Error: " + e2.getMessage());
					
				} catch (SQLException e1) {

					JOptionPane.showMessageDialog(viewRegister.getFrame(), "Error: No ha podido crearse el usuario: " + e1.getMessage());
				}
				

				
				
			}
		});
	}
}
