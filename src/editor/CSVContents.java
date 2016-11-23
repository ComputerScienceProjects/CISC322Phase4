// $Id: TextContents.java,v 1.0 2012/10/04 13:57:18 dalamb Exp $
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
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import java.awt.Dimension;

/**
 * Internal representation of a text document.
 *<p>
 * Copyright 2010-2011 David Alex Lamb.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
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
 	public int getRowCount() { return values.size(); }
    
    @Override
 	public int getColumnCount() { return values.get(0).length; }
    
    @Override    
	public Object getValueAt(int row, int col) {
		return values.get(row)[col];
	}
    
    public Object[] getRowAt(int row) {
    	return values.get(row).clone();	// Sufficient
    }
    
    public Object[] getColumnAt(int col) {
    	int height = values.size();
    	Object[] theColumn = new Object[height];
    	for(int row = 0; row < height; row++)
    	{
    		theColumn[row] = getValueAt(row, col);
    	}
    	return theColumn;
    }
    	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		values.get(row)[col] = (String)value;
	}
    
    /**
     * Reads the entire document, and closes the stream from which it is read.
     * @param in Where to read the document from.
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
	
	this.fireTableStructureChanged();

	//System.err.println("Done open");
    } // end method open

    /**
     * Writes the entire document.
     * @param out Where to write the document
     * @throws IOException if any I/O errors occur.
     */
    public void write(Writer out)  throws IOException
    {
    CSVWriter csvw = new CSVWriter(out);
    csvw.writeAll(values);
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
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream.
     */
    public void save(OutputStream out) throws IOException {
	try {
		write(new OutputStreamWriter(out));
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
    /*
	try {
	    // return new StringBytesInputStream(this);
	    return new StringSequenceInputStream(this);
	} catch (Exception e) {
	    throw new DocumentException(e);
	}
	*/
    } // end getContentStream
	
    /**
     * Gets a substring of the text of the document.
     * A variant on {@link javax.swing.text.Document#getText} that raises no
     *  exceptions.
     * @param start Index of the first character in the substring.
     * @param length Length of the substring
     * @return the substring, or null if the supposed substring extends beyond
     *   the bounds of the text.
     */
    /*
    public String safelyGetText(int start, int length) {
	try {
	    return getText(start,length);
	} catch (Exception e) {
	    return null;
	}
	
    } // end safelyGetText 
    */

} // end CSVContents
