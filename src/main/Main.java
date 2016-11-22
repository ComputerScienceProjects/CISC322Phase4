package main;
/**
 * CISC 322 Group Project Phase 3 
 * @authors Ian Chew, Sabri Alouache, Josh Taylor, Stuart Rutty
 * @version 1.0
 */

//For main
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.List;
import java.io.IOException;
import com.opencsv.CSVReader;
import javax.swing.SwingUtilities;

//For initializeGUI
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class Main {

	
	private static void initializeGUI(List<String[]> values, String filename) {
		//Create the window.
		JFrame frame = new JFrame(filename);
		//Make sure the program exits when the window is closed.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Add GUI elements
		
		TableModel model = new AbstractTableModel() {
			public int getRowCount() { return values.size(); }
			public int getColumnCount() { return values.get(0).length; }
			public Object getValueAt(int row, int col) {
				return values.get(row)[col];
			}
			
		};
		
		JTable table = new JTable(model);
		//Stretch the table to fill
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(new Dimension(500,200));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setOpaque(true);
		frame.setContentPane(scrollPane);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static List<String[]> readCSV(String filename) throws IOException {
		
		InputStream in = new FileInputStream(filename);
		Reader r = new InputStreamReader(in);
		
		//csvr = csv reader
		CSVReader csvr = new CSVReader(r);
		//Read in the .csv file.
		List<String[]> tableOfValues = csvr.readAll();
		csvr.close(); //Close the input file.
		
		return tableOfValues;
	}
	
	/**
	 * This method opens an InputStream, reads in a .csv file, and displays it as a table.
	 * @param args args[0] is the input .csv file.
	 * @throws IOException if there is an error accessing the input and output files.
	 */
	/*
	public static void main(String args[]) throws IOException {
		
		if (args.length < 1) {
			System.out.println("Error: This program expects one argument: The path to the input .csv file.");
			return;
		}
		
		List<String[]> tableOfValues = readCSV(args[0]);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initializeGUI(tableOfValues,args[0]);
			}
		});
	}*/
	
}
