package editor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
// For documentation purposes, import only those classes from edfmwk that are
// actually used.
import ca.queensu.cs.dal.edfmwk.Application;
import ca.queensu.cs.dal.edfmwk.act.DefaultAction;
import ca.queensu.cs.dal.edfmwk.win.CommonWindow;
import ca.queensu.cs.dal.flex.log.Log;
/**
 * {@link javax.swing.Action} for implementing "Move Row(s) Up" functionality.
 *<p>
 * Copyright 2016 CISC 322 Group 7.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 * 
 * @author CISC 322 Group 7
 * @version 1.0
 */
public class MoveRowUpAction extends CSVAction {
	/**
     * Constructs a row movement action -- move selected row(s) right.
     */
    public MoveRowUpAction() {
	super("Move Row(s) Up");
    } // end constructor MoveRowUpAction

    /**
     * Move the selected row up.
     * If multiple rows are selected, move multiple rows up.
     * @param con CSV document to change.
     * @param startRow Index of the first row to change.
     * @param startCol Index of the first column to change.
     * @param endRow Index one beyond the last row to change.
     * @param endCol Index one beyond the last column to change.
     */
    protected void changeCSV(CSVContents con, int startRow, int startCol, int endRow, int endCol) {
    try {
    	if(startRow > 0) {	// A valid row is selected. (0 is invalid, since it's farthest up.)
    		// Store the row that will be displaced downwards by the move.
    		Object[] oldRow = con.getRowAt(startRow - 1);
    		int width = con.getColumnCount();
    		for(int row = startRow; row < endRow; row++) {
    			// Get the row to be moved upwards.
    			Object[] thisRow = con.getRowAt(row);
    			// Move its values to the row above it.
    			for(int col = 0; col < width; col++) {
    				con.setValueAt(thisRow[col], row-1, col);
    			}
    		}
    		// Replace the lowest selected column with the displaced row.
			for(int col = 0; col < width; col++) {
				con.setValueAt(oldRow[col], endRow - 1, col);
			}
			//Tell the listeners that we changed the data in the table.
			con.fireTableDataChanged();
    	} else {
    		throw new Exception();
    	}
    } catch (Exception e) {
    	if(startRow < 0) {
    		JOptionPane.showMessageDialog(null, "Please select a row in order to perform that action.", "No Row Selected", JOptionPane.ERROR_MESSAGE);
    		e.printStackTrace();
    	}
    }
    } // end changeCSV
} // end class MoveRowUpAction
