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
public class MoveColumnRightAction extends CSVAction {
    /**
     * Constructs a column movement action -- move selected column right.
     */
    public MoveColumnRightAction() {
	super("Move Column Right");
    } // end constructor MoveColumnLeftAction

    /**
     * Move the selected column right.
     * If multiple columns are selected, move multiple columns left.
     * @param con Text document to change.
     * @param start Index of the first character to change.
     * @param end Index one beyond the last character to change.
     */
    protected void changeCSV(CSVContents con, int startRow, int startCol, int endRow, int endCol) {
    try {
    	if(endCol < con.getColumnCount()) {	// A valid column is selected. (con.getColumnCount() is invalid, since it's farthest right.)
    		// Store the column that will be displaced left.
    		Object[] oldCol = con.getColumnAt(endCol);
    		int height = con.getRowCount();
    		for(int col = endCol-1; col >= startCol; col--) {
    			// Get the column to be moved.
    			Object[] thisCol = con.getColumnAt(col);
    			// Move its values to the new column.
    			for(int row = 0; row < height; row++) {
    				con.setValueAt(thisCol[row], row, col+1);
    			}
    		}
    		// Replace the leftmost selected column with the displaced column.
			for(int row = 0; row < height; row++) {
				con.setValueAt(oldCol[row], row, startCol);
			}
    	}
    } catch (Exception e) {
    	e.printStackTrace();
    }
    } // end changeCSV
} // end class MoveColumnRightAction
