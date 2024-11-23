package xgf.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import xgf.model.*;
import xgf.view.*;

/**
 * @author Xavi
 */
public class Controller {
	
	private Login viewLogin;
	private AdminPanel viewAdmin;
	private ClientPanel viewClient;
	private Register viewRegister;
	
	private Connection connection;
	
	private User user;
	
	public Controller() {
		
		viewLogin = new Login();
		initLoginEventHandler();
	}

	private void initLoginEventHandler() {
		
		viewLogin.getBtnLogin().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to log the user in
			 */
			public void actionPerformed(ActionEvent e) {
				
				String userName = viewLogin.getTextUser().getText();
				char[] userPassword = viewLogin.getPasswordUser().getPassword();
				
				user = null;
				
				try {
					
					userLogin(userName, userPassword);
					
				} catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException e1) {
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: Usuario o contraseña incorrectos");
				}
				
				viewLogin.getTextUser().setText("");
				viewLogin.getPasswordUser().setText("");
			}

			/**
			 * Logs the user into the database with the provided userName and password.
			 * If the login is successful, instantiate the relevant panel and close the Login view
			 * @param userName User name of the user to do log-in
			 * @param userPassword Password of the user to do log-in
			 * @throws NoSuchAlgorithmException When it cannot generate a hash 
			 * related to the password
			 * @throws SQLException When a connection to the database is not able to be made
			 * @throws ClassNotFoundException When the specified driver is not found
			 */
			private void userLogin(String userName, char[] userPassword) throws NoSuchAlgorithmException, SQLException, ClassNotFoundException {
				
				user = new User(userName,userPassword);
				
				Database database = new Database("population");
				
				connection = database.connectToDatabase(user.getName(), user.getPassword());
				
				try {
					
					user.setType(database.getUserType(connection,user.getName()));
					
				} catch (SQLException e1) {
					
					user.setType("client");
				}
				
				if (user.getType().equals("admin")) {
					
					viewAdmin = new AdminPanel();
					initAdminEventHandlers();
					
				}else if(user.getType().equals("client")) {
					
					viewClient = new ClientPanel();
					initClientEventHandlers();
				}
				
				viewLogin.getFrame().setVisible(false);
			}
		});
	}

	/**
	 * Generates multiple event handlers related to the AdminPanel view
	 */
	private void initAdminEventHandlers() {
		
		viewAdmin.getBtnImportCSV().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, import a CSV file from a location 
			 * chosen by the user
			 * @param e Event executed
			 */
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setFileFilter(new FileNameExtensionFilter("Archivo CSV", "csv"));
				
				if (chooser.showOpenDialog(viewAdmin.getFrame()) == JFileChooser.APPROVE_OPTION) {
					
					File chosenFile = chooser.getSelectedFile();
					
					try {
						
						processCSV(chosenFile);
							
					} catch (IOException | ParserConfigurationException | TransformerException e1) {
						
						JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error al procesar CSV: " + e1.getMessage());
					}
				}
			}

			/**
			 * Generate multiple XML File from the contents of a provided CSV File, 
			 * populate a JTextArea in the AdminPanel with the same contents and 
			 * insert everything into the database
			 * @param chosenFile File with the needed data to generate the XML
			 * @throws IOException When the contents of the file cannot be accessed
			 * @throws ParserConfigurationException When the XML file cannot be instantiated
			 * @throws TransformerException When the XML file cannot be populated
			 */
			private void processCSV(File chosenFile) throws IOException, ParserConfigurationException, TransformerException {
				
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

			/**
			 * Recreates the population table with the contents of the previously created XML
			 * @param firstLineColumns
			 */
			private void insertXMLToTable(String[] firstLineColumns) {
				
				try {
					
					Database.removeTable(connection, "population");
					Database.createTable(connection, "population", firstLineColumns);
					
					List<String> fileValues = new ArrayList<String>();
					
					for (File file : XML.getCreatedFiles()) {
						
						fileValues = XML.getAttributesAndValues(file);
						
						Database.insertPopulation(connection, fileValues);
					}
					
					JOptionPane.showMessageDialog(viewAdmin.getFrame(), "CSV Importado");

				} catch (SQLException | ParserConfigurationException | SAXException | IOException e) {
					
					JOptionPane.showMessageDialog(viewAdmin.getFrame(), "Error: No ha sido posible insertar XML en base de datos: " + e.getMessage());
				}
			}
		});
		
		/**
		 * When the specified action is performed, it generates a new Register view 
		 * with it's event handler
		 */
		viewAdmin.getBtnNewUser().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				viewRegister = new Register();
				initRegisterEventHandlers();
			}
		});
		
		/**
		 * When the specified action is performed, it triggers a log-out 
		 * related to the AdminPanel view 
		 */
		viewAdmin.getBtnLogout().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
					
				doLogout(viewAdmin);
			}
		});

		/**
		 * When the specified action is performed, it triggers a select query 
		 * related to the AdminPanel view 
		 */
		viewAdmin.getBtnNewQuery().addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				
				getQueryResult(viewAdmin);
			}
		});
		
		/**
		 * When the specified action is performed, it triggers a CSV export 
		 * related to the AdminPanel view 
		 */
		viewAdmin.getBtnExportCSV().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				exportCSV(viewAdmin);
			}
		});
	}
	
	/**
	 * Generates multiple event handlers related to the Client view
	 */
	private void initClientEventHandlers() {
		
		viewClient.getBtnLogout().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, it triggers a log-out 
			 * related to the ClientPanel view 
			 */
			public void actionPerformed(ActionEvent e) {
					
				doLogout(viewClient);
			}
		});
		
		viewClient.getBtnNewQuery().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, it triggers a select query 
			 * related to the ClientPanel view 
			 */
			public void actionPerformed(ActionEvent e) {
				
				getQueryResult(viewClient);
			}
		});
		
		viewClient.getBtnExportCSV().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, it triggers a CSV export 
			 * related to the ClientPanel view 
			 */
			public void actionPerformed(ActionEvent e) {
				
				exportCSV(viewClient);
			}
		});
	}

	/**
	 * Generates an event handler related to the Register view
	 */
	private void initRegisterEventHandlers() {
	
		viewRegister.getBtnRegister().addActionListener(new ActionListener() {
			
			/**
			 * Tries to create a new user if the listener is triggered by 
			 * using the userName and password that Register contains
			 * @param e Event executed
			 */
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					String userName = viewRegister.getTextUser().getText();
					char[] userPassword = viewRegister.getPasswordUser().getPassword();
					char[] userPasswordRepeated = viewRegister.getPasswordUserRepeat().getPassword();
					
					if (!userName.isEmpty() && !userPassword.equals(null) && !userPasswordRepeated.equals(null)) {
						
						user = new User(userName, userPassword);
						
						if (!(user.getPassword().equals(User.getHash(userPasswordRepeated)))) {
	
							throw new NullPointerException("Las contraseñas no coinciden");
						}
						
						if (connection == null) {
	
							throw new NullPointerException("No ha podido conectarse con la base de datos");
						}
						
						if (Database.setUpClient(connection, user.getName(), user.getPassword())) {
	
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

	/**
	 * Tries to log out of the application by closing the Connection and current view
	 * @param view Target to close
	 */
	private void doLogout(ViewType view) {
		
		try {
			
			connection.close();
			Database.emptyCurrentQuery();
			
			viewLogin = new Login();
			initLoginEventHandler();
			
			view.getFrame().setVisible(false);
			
		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(view.getFrame(), "No se ha podido cerrar sesión: " + e.getMessage());
		}
		
	}

	/**
	 * Generates a select query and populates a table of the provided ViewType with it's result
	 * @param view Target ViewType to get the query and table
	 */
	private void getQueryResult(ViewType view) {
		
		String customQuery = view.getTextNewQuery().getText().toUpperCase();
		
		if (Database.isValidQuery(customQuery)) {
			
			try {
				
				Database.customSelect(connection,customQuery);

				DefaultTableModel tableModel = new DefaultTableModel(Database.getCurrentQueryRows(), Database.getCurrentQueryHeader());
				
				view.getTableQueryResult().setModel(tableModel);
				
			} catch (SQLException e1) {
				
				Database.emptyCurrentQuery();
				
				JOptionPane.showMessageDialog(view.getFrame(), "Error: No han podido mostrarse las filas. ¿Has comprobado lo que estás buscando? ");
			}
			
		}else {
			
			Database.emptyCurrentQuery();
			
			JOptionPane.showMessageDialog(view.getFrame(), "Error: El formato de la consulta no es válido");
		}
	}

	/**
	 * Generate a CSV file with a location chosen by the user
	 * @param view Target ViewType to anchor the File chooser and pop ups
	 */
	private void exportCSV(ViewType view) {

		if (Database.getCurrentQueryHeader() != null) {
			
			String fileExtension = "csv";
			
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setFileFilter(new FileNameExtensionFilter("Archivo " + fileExtension.toUpperCase(), fileExtension));
			
			if (chooser.showOpenDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
				
				File chosenFile = getFileWithExtension(chooser.getSelectedFile(), fileExtension);
				
				try {
					
					saveCSV(chosenFile,";");
					
					JOptionPane.showMessageDialog(view.getFrame(), "Archivo creado");
						
				} catch (IOException e1) {
					
					JOptionPane.showMessageDialog(view.getFrame(), "Error: No ha podido crearse el archivo: " + e1.getMessage());
				}
			}
			
		}else {
			
			JOptionPane.showMessageDialog(view.getFrame(), "Error: No hay datos que exportar");
		}
		
	}

	/**
	 * If the provided file does not end with the provided extension, 
	 * add it to the end of it's name
	 * @param chosenFile Target file to check for it's extension
	 * @param fileExtension Extension to check for
	 * @return Return the same or a new File if the extension was not present
	 */
	private File getFileWithExtension(File chosenFile, String fileExtension) {
		
		String extension = "." + fileExtension;
		
		if (!chosenFile.getAbsolutePath().endsWith(extension)) {
			
			chosenFile = new File(chosenFile + extension);
		}
		
		return chosenFile;
	}

	/**
	 * Tries to populate a specified File with a query previously saved in the Database class
	 * @param chosenFile Target File to be populated
	 * @param separator Separator to divide each column inside the File
	 * @throws IOException When the target File cannot be interacted with
	 */
	private void saveCSV(File chosenFile, String separator) throws IOException {
		
		BufferedWriter writer = Files.newBufferedWriter(chosenFile.toPath(), StandardCharsets.ISO_8859_1);
		
		String queryFirstLine = String.join(separator, Database.getCurrentQueryHeader());
		
		String[][] queryRestOfLines =  Database.getCurrentQueryRows();
		
		writer.write(queryFirstLine);
		
		for (String[] line : queryRestOfLines) {
			
			writer.newLine();
			writer.write(String.join(separator, line));
		}
		
		writer.close();
	}
}
