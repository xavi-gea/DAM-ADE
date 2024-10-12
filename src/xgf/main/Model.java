package xgf.main;

import java.awt.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Class to manage information returned by the Controller and operate over files and folders
 * @author Xavi
 * @version 1.0
 */
public class Model {
	
	private File rootDirectory;
	private String directoryContent = "";
	
	private boolean checkCase = true;
	private boolean checkAccent = true;
	private boolean checkReplace = false;

	/**
	 * Show content inside directory chosen by the user
	 * @param contentPane Main panel to anchor the rest of the content
	 */
	public void showDirectoryContent(JPanel contentPane) {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if (chooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
			
			directoryContent = "";
			rootDirectory = chooser.getSelectedFile();
			
			if (isEmptyDirectory(rootDirectory)) {
				
				JOptionPane.showMessageDialog(contentPane, "El directorio está vacío");
				
			}else {
				
				try {
					
					TextArea directoryContentContainer = new TextArea(getDirectoryContent(rootDirectory));
					directoryContentContainer.setEditable(false);

					JOptionPane.showMessageDialog(contentPane,
							new JScrollPane(directoryContentContainer));

				}catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Show the number of found text inside a directory path
	 * @param contentPane Main panel to anchor the rest of the content
	 * @param textToFindInDirectory Text to find inside a given directory path
	 * @param checkCase Check to take case letters into account
	 * @param checkAccent Check to take letters with accent into account
	 * @param checkReplace Check to take into account if text should be replaced or only found
	 * @param textToReplaceInDirectory Text to replace found text with
	 */
	public void showMatchInDirectory(JPanel contentPane, JTextField textToFindInDirectory, JCheckBox checkCase, JCheckBox checkAccent, JCheckBox checkReplace, JTextField textToReplaceInDirectory) {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if (chooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
			
			directoryContent = "";
			rootDirectory = chooser.getSelectedFile();
			
			this.checkCase = checkCase.isSelected();
			this.checkAccent = checkAccent.isSelected();
			this.checkReplace = checkReplace.isSelected();
			
			try {

				if (isEmptyDirectory(rootDirectory)) {

					throw new FileNotFoundException("El directorio está vacío");
				}

				if (textToFindInDirectory.getText().equals("")) {

					throw new NullPointerException("El texto está vacío");
				}

				if (this.checkReplace && textToReplaceInDirectory.getText().equals("")) {

					throw new NullPointerException("El texto a reemplazar está vacío");
				}

				if (this.checkReplace) {

					TextArea matchInDirectoryContainer = new TextArea(getMatchInDirectory(rootDirectory,
							textToFindInDirectory.getText(), textToReplaceInDirectory.getText()));
					matchInDirectoryContainer.setEditable(false);

					JOptionPane.showMessageDialog(contentPane, new JScrollPane(matchInDirectoryContainer));

				} else {

					TextArea matchInDirectoryContainer = new TextArea(
							getMatchInDirectory(rootDirectory, textToFindInDirectory.getText()));
					matchInDirectoryContainer.setEditable(false);

					JOptionPane.showMessageDialog(contentPane, new JScrollPane(matchInDirectoryContainer));
				}

			} catch (Exception e) {

				JOptionPane.showMessageDialog(contentPane, "Error: " + e.getMessage());
			}
		}
		
	}

	/**
	 * Show detail of files found inside directory path
	 * @param directory Directory to find content in
	 * @return String Content inside directory
	 */
	private String getDirectoryContent(File directory) {
		
		for (File element : directory.listFiles()) {
			
			if (element.canRead()) {
				
				if (element.isFile()) {
					
					directoryContent += getFileDetails(element) + "\n";
				
				}else if(element.isDirectory() && !isEmptyDirectory(element)) {
					
					getDirectoryContent(element);
				}
			}
		}
		
		return directoryContent;
	}

	/**
	 * Return the number of found text in a given directory path 
	 * @param directory Directory to look for matches
	 * @param text Text to look for
	 * @return String Number of coincidences found inside a given directory
	 */
	private String getMatchInDirectory(File directory, String text) {
	
		for (File element : directory.listFiles()) {
			
			if (element.isFile()) {
				
				if (element.canRead()) {

					directoryContent += getPathFromDirectory(element, rootDirectory) + " ("
							+ getNumberOfMatchesToFind(element, text) + " coincidencias)" + "\n";

				}else {
					
					directoryContent += element.getName() + "(0 coincidencias)" + "\n";
				}
				
			}else if (element.isDirectory() && element.canRead() && !isEmptyDirectory(element)) {
				
				getMatchInDirectory(element, text);
			}
		}
		
		return directoryContent;
	}

	/**
	 * Returns the number of matches found inside a file for the specified text
	 * @param file File to look for text in
	 * @param text Text to look for
	 * @return integer Number of matches found inside file
	 */
	private int getNumberOfMatchesToFind(File file, String text) {
		
		int matches = 0;
		
		String regexPattern = getRegexPattern(text);
		
		if (fileHasExtension(file, "pdf")) {
			
			String pdfText = "";
			
			try {
				
				pdfText = new PDFTextStripper().getText(PDDocument.load(file));
				
			} catch (IOException e) {

				pdfText = "";
			}
			
			if (!pdfText.equals("")) {
				
				for (String word : pdfText.split("\\s")) {
					
					if (word.matches(regexPattern)) {
						
						matches++;
					}
				}
			}
			
		}else {
			
			List<String> fileLines = new ArrayList<String>();
			
			try {
				
				fileLines = Files.readAllLines(file.toPath());
				
			} catch (IOException e) {
				
				fileLines = new ArrayList<String>();
			}
			
			for (String line : fileLines) {
				
				for (String word : line.split("\\s")) {
					
					if (word.matches(regexPattern)) {
						
						matches++;
					}
				}
			}
		}
		
		return matches;
	}

	/**
	 * Return the number of replaced text in a given directory path
	 * @param directory Directory to look for matches
	 * @param text Text to look for
	 * @param textToReplaceWith Text that replaces the original
	 * @return String Text that indicates the number of replaced text
	 * @throws IOException Exception in case it cannot create a file with the replaced text
	 */
	private String getMatchInDirectory(File directory, String text, String textToReplaceWith) throws IOException {
		
		for (File element : directory.listFiles()) {
			
			if (element.isFile()) {
				
				if (element.canRead()) {
				
					directoryContent += getPathFromDirectory(element,rootDirectory) + " ("+ getNumberOfMatchesAndReplace(directory,element,text,textToReplaceWith) + " reemplazos)" + "\n";
					
				}else {
					
					directoryContent += element.getName() + "(0 reemplazos)" + "\n";
				}
				
			}else if (element.isDirectory() && element.canRead() && !isEmptyDirectory(element)) {
				
				getMatchInDirectory(element, text, textToReplaceWith);
			}
		}
		
		return directoryContent;
	}

	/**
	 * Get the number of matches and generate a file with the changed text
	 * @param directory Directory to create the new file
	 * @param file File to get the text from
	 * @param text Original text to look for
	 * @param textToReplaceWith Text that replaces the original
	 * @return integer Number of matches
	 * @throws IOException Exception in case it cannot create a file with the replaced text
	 */
	private int getNumberOfMatchesAndReplace(File directory, File file, String text, String textToReplaceWith) throws IOException {
		
		int matches = 0;
		boolean encontrado = false;
		
		String regexPattern = getRegexPattern(text);
		
		List<String> fileLines = new ArrayList<String>();
		
		try {
			
			fileLines = Files.readAllLines(file.toPath());
			
		} catch (IOException e) {
			
			fileLines = new ArrayList<String>();
		}
		
		List<String> newFileLines = new ArrayList<String>();
		
		for (String line : fileLines) {
			
			encontrado = false;
			
			for (String word : line.split("\\s")) {
				
				if (word.matches(regexPattern)) {
					
					matches++;
					encontrado = true;
				}
			}
			
			if (encontrado) {
				
				newFileLines.add(line.replaceAll(regexPattern, textToReplaceWith));
				
			}else {
				
				newFileLines.add(line);
			}
		}
		
		if (matches > 0) {
			
			createNewFile(file, directory, newFileLines);
		}
		
		return matches;
	}

	/**
	 * Returns multiple details about the given file (Path, size and last modified date)
	 * @param file File to get details from
	 * @return String Details about the given file
	 */
	private String getFileDetails(File file) {
		
		String result = "";
		
		result += getPathFromDirectory(file, rootDirectory);
		result += " (";
		
		String fileSize = "0";
		
		try {
			
			fileSize = (Files.size(file.toPath()) / 1024) + " KB";
			
		} catch (Exception e) {
			
			fileSize = "0";
			
		}finally {
			
			result += fileSize;
		}
		
		result += " - ";
		
		result += new Date(file.lastModified());
		
		result += ")";;
		
		return result;
	}

	/**
	 * Creates a new file with the provided lines, in a specific directory and with a variation of the provided file name
	 * @param oldFile File to take the name from
	 * @param directory Directory to create the file in
	 * @param newDirectoryLines Lines to put in the new file
	 * @throws IOException Exception in case it cannot create a file with the replaced text
	 */
	private void createNewFile(File oldFile, File directory, List<String> newDirectoryLines) throws IOException {
		
		String newFileName = directory.getPath() + "\\" + "NEW_" + oldFile.getName();
		
		try {
			
			Files.write(Paths.get(newFileName), newDirectoryLines, StandardCharsets.UTF_8);
			
		} catch (Exception e) {
			
			throw new IOException("No se ha podido crear el archivo: " + e.getMessage());
		}
	}

	/**
	 * Checks if the given file has the given extension
	 * @param file File to get it's extension
	 * @param extension Extension to check in the provided file
	 * @return boolean If the provided file has the provided extension
	 */
	private boolean fileHasExtension(File file, String extension) {
		
		String extensionArchivo = "";
		
		int extensionPosition = file.getName().lastIndexOf(".");
		
		if (extensionPosition != -1) {
		
			extensionArchivo = file.getName().substring(extensionPosition + 1);
		}
		
		if(extensionArchivo.equals(extension)) {
			
			return true;
			
		}else {
			
			return false;
		}
	}

	/**
	 * Returns a path that starts at the given directory and stops at the given file
	 * @param file File to get it's path
	 * @param directory Directory to start the path
	 * @return String The absolute path changed to start at the given directory
	 */
	private String getPathFromDirectory(File file, File directory) {
		
		String elementPath = file.getAbsolutePath();
		
		return elementPath.substring(elementPath.indexOf(directory.getName()));
	}

	/**
	 * Returns if the given directory is empty
	 * @param selectedDirectory Directory to check if it's empty
	 * @return boolean If the given directory is empty or not
	 */
	private boolean isEmptyDirectory(File selectedDirectory) {
		
		if (selectedDirectory.list().length == 0) {
			
			return true;
			
		}else {
			
			return false;
		}
	}

	/**
	 * Generates and returns a pattern
	 * @param text Text to add at the end of the pattern
	 * @return String The pattern to look for, adapted to the checks selected in the application
	 */
	private String getRegexPattern(String text) {
		
		String regexPattern = "";
		
		regexPattern = !checkCase ? "(?i)" + regexPattern : regexPattern;
		regexPattern = !checkAccent ? regexPattern + "\\p{L}" : regexPattern;
		
		return regexPattern += text;
	}
}
