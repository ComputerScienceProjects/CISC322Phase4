package editor;
import java.io.*;
//import java.util.*;
import ca.queensu.cs.dal.edfmwk.doc.DocumentException;
import ca.queensu.cs.dal.edfmwk.doc.StringSequence;
import ca.queensu.cs.dal.edfmwk.doc.StringSequenceInputStream;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.List;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javax.swing.SwingUtilities;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Internal representation of a text document.
 *<p>
 * Copyright 2016 CISC 322 Group 7.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 * 
 * @author CISC 322 Group 7
 * @version 1.0
 */
public class CSVContents
    extends javax.swing.table.AbstractTableModel
//    implements StringSequence
{
	//Internal table of values.
    private List<String[]> values;
    
    private final static long serialVersionUID = 1L;
    

    
    /**
     * Constructs an empty text file contents.
     */
    
    public CSVContents() {
	super();
	values = new ArrayList<String[]>();

	String[] s = {};
	
	values.add(s);
    } // end constructor
	
    @Override
    /**
     * Return the number of rows in the table.
     * @return the number of rows.
     */
 	public int getRowCount() { return values.size(); }
    
    @Override
    /**
     * Return the number of columns in the table.
     * @return the number of columns.
     */
 	public int getColumnCount() { return values.get(0).length; }
    
    @Override    
    /**
     * Get the specified value in the table.
     * @param row The index of the row in the table.
     * @param col The index of the column in the table.
     * @return the value at (row,col).
     */
	public Object getValueAt(int row, int col) {
		return values.get(row)[col];
	} //end getValueAt
    
    /**
     * Get the specified row in the table.
     * @param row The index of the row being retrieved.
     * @return The row as a array of Objects.
     */
    public Object[] getRowAt(int row) {
    	return values.get(row).clone();	// Sufficient
    } //end getRowAt
    
    /**
     * Get the specified column in the table.
     * @param col The index of the column being retrieved.
     * @return The column as a array of Objects.
     */    
    public Object[] getColumnAt(int col) {
    	int height = values.size();
    	Object[] theColumn = new Object[height];
    	for(int row = 0; row < height; row++)
    	{
    		theColumn[row] = getValueAt(row, col);
    	}
    	return theColumn;
    } // end getColumnAt
    	
    /**
     * Returns true if a cell given cell is editable, which is always true.
     * @param row Ignored.
     * @param col Ignored.
     * @return <b>true</b>.
     */
	@Override
	public boolean isCellEditable(int row, int col) {
		return true; //All cells are editable
	} //end isCellEditable
	
	/**
	 * Sets the value at the given location in the table.
	 * @param value The value to set (String).
	 * @param row The row to set the value in.
	 * @param col The column to set the value in.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		values.get(row)[col] = (String)value;
		//Tell listeners we updated this element.
		this.fireTableCellUpdated(row, col);
	} //end setValueAt
	
    /**
     * Reads the entire document, and closes the stream from which it is read.
     * @param in The InputStream to read the document from.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream.
     */
    public void open(InputStream in)
	throws IOException
    {
    //System.err.println("Open...");		
	
    Reader r = new InputStreamReader(in);
	
	//csvr = csv reader
	CSVReader csvr = new CSVReader(r);
	//Read in the .csv file.
	values = csvr.readAll();
	csvr.close(); //Close the input file.
	
	//The table's whole structure has changed, so we need to notify any listeners.
	this.fireTableStructureChanged();

	//System.err.println("Done open");
    } // end method open

    /**
     * Writes the entire document.
     * @param out Where to write the document
     * @param jta JTable that we're saving from, tells write() the order to write the rows in.
     * @throws IOException if any I/O errors occur.
     */
    public void write(Writer out, JTable jta)  throws IOException
    {
    CSVWriter csvw = new CSVWriter(out);
    for (int i=0; i<this.getRowCount();i++) {
    	//Write the ith element from the current sorted order.
    	int index = jta.convertRowIndexToModel(i);
    	csvw.writeNext(values.get(index));
    }
    //Close the writer
    csvw.close();
    /*
	//System.err.println("Writing...");
	PrintWriter pr = new PrintWriter(out);
	int docLength = getLength();
	int lengthLeft = docLength;
	int pos = 0;
	while(lengthLeft>0) {
	    int len = Math.min(bufferSize,lengthLeft);
	    try {
		String line = getText(pos,len);
		pr.print(line);
		pos += len;
		lengthLeft -= len;
	    } catch (javax.swing.text.BadLocationException e) {
		break;
	    }
	}
	pr.close();
	//System.err.println("Written.");
	*/
    } // end method write

    /**
     * Saves the entire document.
     * @param out Where to write the document.
     * @param jta JTable that we're saving from, tells save the order to write the rows in.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream.
     */
    public void save(OutputStream out,JTable jta) throws IOException {
	try {
		write(new OutputStreamWriter(out),jta);
	} catch (Exception e) {
	    out.close();
	    //	    throw new IOException(e);
	    throw new IOException(e.getLocalizedMessage());
	}
    } // end save

    /**
     * Gets an input stream from which the document contents can be read as a
     *  stream of bytes.  This is required when running in a sandbox, where
     *  {@link javax.jnlp.FileSaveService#saveAsFileDialog} does not provide a
     *  means of supplying an output stream to which to write the internal
     *  representation. Document managers should avoid using this method
     *    wherever possible, preferring {@link #save} instead.
     * @throws DocumentException if such a stream cannot be created.
     */
    
    public InputStream getContentsStream() throws DocumentException
    {
    StringWriter sw = new StringWriter();
    try {
    	CSVWriter csvw = new CSVWriter(sw);
    	csvw.writeAll(values);
    	csvw.close();
    } catch (Exception e) {
    	throw new DocumentException(e);
    }
    String s = sw.toString();
    return new ByteArrayInputStream(s.getBytes());
    } // end getContentStream
	


} // end CSVContents
