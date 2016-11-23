// $Id: DeleteAction.java,v 1.0 2012/10/04 13:57:18 dalamb Exp $
package editor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
// For documentation purposes, import only those classes from edfmwk that are
// actually used.
import ca.queensu.cs.dal.edfmwk.Application;
import ca.queensu.cs.dal.edfmwk.act.DefaultAction;
import ca.queensu.cs.dal.edfmwk.win.CommonWindow;
import ca.queensu.cs.dal.flex.log.Log;
/**
 * {@link javax.swing.Action} for implementing "Delete" functionality.
 *<p>
 * Copyright 2010 David Alex Lamb.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public class MoveRowDownAction extends CSVAction {
	/**
     * Constructs a row movement action -- move selected row(s) right.
     */
    public MoveRowDownAction() {
	super("Move Row(s) Down");
    } // end constructor MoveRowDownAction

    /**
     * Move the selected row down.
     * If multiple rows are selected, move multiple rows down.
     * @param con CSV document to change.
     * @param startRow Index of the first row to change.
     * @param startCol Index of the first column to change.
     * @param endRow Index one beyond the last row to change.
     * @param endCol Index one beyond the last column to change.
     */
    protected void changeCSV(CSVContents con, int startRow, int startCol, int endRow, int endCol) {
    try {
    	if(endRow < con.getRowCount()) {	// A valid row is selected. (The last row is invalid, since it's farthest up.)
    		// Store the row that will be displaced upwards by the move.
    		Object[] oldRow = con.getRowAt(endRow);
    		int width = con.getColumnCount();
    		for(int row = endRow-1; row >= startRow; row--) {
    			// Get the row to be moved downwards.
    			Object[] thisRow = con.getRowAt(row);
    			// Move its values to the row below it.
    			for(int col = 0; col < width; col++) {
    				con.setValueAt(thisRow[col], row+1, col);
    			}
    		}
    		// Replace the lowest selected column with the displaced row.
			for(int col = 0; col < width; col++) {
				con.setValueAt(oldRow[col], startRow, col);
			}
			//Tell the listeners that we changed the data in the table.
			con.fireTableDataChanged();
    	}
    } catch (Exception e) {
    	e.printStackTrace();
    }
    } // end changeCSV
} // end class MoveRowDownAction
