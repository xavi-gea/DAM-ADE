package xgf.controller;

import java.awt.FileDialog;
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
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import xgf.model.Database;
import xgf.model.Model;
import xgf.model.User;
import xgf.model.XML;
import xgf.view.AdminPanel;
import xgf.view.ClientPanel;
import xgf.view.Login;
import xgf.view.Register;
import xgf.view.View;
import xgf.view.ViewType;

public class Controller {
	
	private View view;
	private Model model;
	
	private Login viewLogin;
	private AdminPanel viewAdmin;
	private ClientPanel viewClient;
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

	private void initAdminEventHandlers() {
		
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
		
		viewAdmin.getBtnNewUser().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				viewRegister = new Register();
				initRegisterEventHandlers();
			}
		});
		
		viewAdmin.getBtnLogout().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
					
				doLogout(viewAdmin);
			}
		});

		viewAdmin.getBtnNewQuery().addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				
				getQueryResult(viewAdmin);
			}
		});
		
		viewAdmin.getBtnExportCSV().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				exportCSV(viewAdmin);
			}
		});
	}
	
	private void initClientEventHandlers() {
		
		viewClient.getBtnLogout().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
					
				doLogout(viewClient);
			}
		});
		
		viewClient.getBtnNewQuery().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				getQueryResult(viewClient);
			}
		});
		
		viewClient.getBtnExportCSV().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				exportCSV(viewClient);
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

	private void doLogout(ViewType view) {
		
		try {
			
			connection.close();
			Database.emptyCurrentQuery();
			
			viewLogin = new Login();
			initLoginEventHandler();
			
			view.getFrame().setVisible(false);
			
		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(viewRegister.getFrame(), "No se ha podido cerrar sesión: " + e.getMessage());
		}
		
	}

	private void getQueryResult(ViewType view) {
		
		String customQuery = view.getTextNewQuery().getText().toUpperCase();
		
		if (Database.isValidQuery(customQuery,user.getType())) {
			
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

	private File getFileWithExtension(File chosenFile, String fileExtension) {
		
		String extension = "." + fileExtension;
		
		if (!chosenFile.getAbsolutePath().endsWith(extension)) {
			
			chosenFile = new File(chosenFile + extension);
		}
		
		return chosenFile;
	}

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
