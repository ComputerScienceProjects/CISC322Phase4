package editor;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.io.*;
//import java.util.*;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
// Import only those classes from edfmwk that are essential, for documentation purposes
import ca.queensu.cs.dal.edfmwk.doc.AbstractDocument;
import ca.queensu.cs.dal.edfmwk.doc.DocumentType;
import ca.queensu.cs.dal.edfmwk.doc.DocumentEvent;
import ca.queensu.cs.dal.edfmwk.doc.DocumentException;
import ca.queensu.cs.dal.edfmwk.doc.DocumentListener;
/**
 * Implementation of a csv document, which is (indirectly) defined in
 * terms of a Swing {@link javax.swing.Document}.
 *<p>
 * Copyright 2016 CISC 322 Group 7.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 * 
 * @author CISC 322 Group 7
 * @version 1.0
 */
public class CSVDocument
    extends AbstractDocument
    implements javax.swing.event.DocumentListener
{
    private CSVContents contents;
    private JTable jta;

    
    //A renderer for right-justifying integer elements.
    private static final DefaultTableCellRenderer rightRenderer;
    //The normal default renderer. Usually align things to the left.
    private static final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    
    static {
    	rightRenderer = new DefaultTableCellRenderer();
    	rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
    }
    /**
     * Constructs a document representation.
     * @param type The type of the document.
     */
    public CSVDocument(DocumentType type) {
	super(type);
	contents = new CSVContents();
//	contents.addDocumentListener(this);
	jta = new JTable(contents) {
		@Override
		public TableCellRenderer getCellRenderer(int row, int col) {
			//Get the sorted version of the row index, if necessary.
			int modelRow = this.convertRowIndexToModel(row);
			if (CSVDocument.isInteger((String)dataModel.getValueAt(modelRow, col))) {
				return rightRenderer;
			} else {
				return defaultRenderer;
			}
		}
	};
	
	
	//Stretch the table to fill.
	jta.setFillsViewportHeight(true);
	jta.setPreferredScrollableViewportSize(new Dimension(500,200));
	//Allow sorting of the table.
	jta.setAutoCreateRowSorter(true);

	JScrollPane scrollPane = new JScrollPane(jta);
	scrollPane.setOpaque(true);
	
	window = scrollPane;
    } // end CSVDocument

    // Text document change listeners: all invoke the framework's own document
    // change listeners.

    /**
     * Gives notification that an attribute or set of attributes changed.
     */
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
	setChanged();
    } // end changedUpdate

    /**
     * Gives notification that there was an insert into the document.
     */
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
	setChanged();
    } // end insertUpdate

    /**
     * Gives notification that a portion of the document has been removed.
     */
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
	setChanged();
    } // end removeUpdate

    /**
     * Saves the entire document.  After this operation completes
     * successfully, {@link #isChanged} returns <b>false</b>
     * @param out Where to write the document.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream; isChanged() is unchanged.
     */
    public void save(OutputStream out) throws IOException {
    //The JTable parameter tells save the order to write out the rows (if sorted).
	contents.save(out,jta);
	setChanged(false);
    } // save

    /**
     * Gets an input stream from which the document contents can be read as a
     *  stream of bytes.  This is required when running in a sandbox, where
     *  {@link javax.jnlp.FileSaveService#saveAsFileDialog} does not provide a
     *  means of supplying an output stream to which to write the internal
     *  representation. Document managers should avoid using this method
     *    wherever possible, preferring {@link #save} instead.
     * @throws IOException if such a stream cannot be created.
     */
    public InputStream getContentsStream() throws DocumentException {
    return contents.getContentsStream();

    } // getContentsStream

    /**
     * Reads the entire document, and closes the stream from which it is read.
     * @param in Where to read the document from.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream.
     */
    public void open(InputStream in)
	throws IOException
    {
	contents.open(in);
	/*
	for (int col=0; col < contents.getColumnCount(); col++) {
		jta.getColumnModel().getColumn(col).setCellRenderer(new CSVRenderer());
	}
	*/
	setChanged(false);
    } // end open

    /**
     * Gets the contents of the csv document, for those few methods within
     *    this package that need direct access (such as actions).
     */
    CSVContents getContents() { return contents; }
    
    /**
     * Check if the input string represents an integer.
     * @param str The string to be check.
     * @return <b>true</b> if <code>str</code> represents an integer.
     */
    private static boolean isInteger(String str) {
    	if (str.isEmpty()) {
    		//Empty strings contain no integers.
    		return false;
    	}
    	if (str.equals("-")) {
    		return false;
    	} else if (str.charAt(0)!='-' && Character.digit(str.charAt(0),10) < 0) {
    		//First character is invalid, so string is not an integer.
    		return false;
    	}
    	//Start at 1, since we already checked the first character.
    	for (int i=1; i<str.length(); i++) {
    		//Character.digit returns -1 if the character is not a digit.
    		if (Character.digit(str.charAt(i),10) < 0) {
    			return false;
    		}
    	}
    	//Every character is of the correct type.
    	return true;
    } // end isInteger

} // end class TextDocument

