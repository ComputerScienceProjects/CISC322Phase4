
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
 * {@link javax.swing.Action} for implementing "Move Column(s) Left" functionality.
 *<p>
 * Copyright 2016 CISC 322 Group 7.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public class MoveColumnLeftAction extends CSVAction {
	/**
     * Constructs a column movement action -- move selected column(s) right.
     */
    public MoveColumnLeftAction() {
	super("Move Column(s) Left");
    } // end constructor MoveColumnLeftAction

    /**
     * Move the selected column left.
     * If multiple columns are selected, move multiple columns left.
     * @param con CSV document to change.
     * @param startRow Index of the first row to change.
     * @param startCol Index of the first column to change.
     * @param endRow Index one beyond the last row to change.
     * @param endCol Index one beyond the last column to change.
     */
    protected void changeCSV(CSVContents con, int startRow, int startCol, int endRow, int endCol) {
    try {
    	if(startCol > 0) {	// A valid column is selected. (0 is invalid, since it's farthest left.)
    		// Store the column that will be displaced rightwards by the move.
    		Object[] oldCol = con.getColumnAt(startCol - 1);
    		int height = con.getRowCount();
    		for(int col = startCol; col < endCol; col++) {
    			// Get the column to be moved leftwards.
    			Object[] thisCol = con.getColumnAt(col);
    			// Move its values to the column beside it to the left.
    			for(int row = 0; row < height; row++) {
    				con.setValueAt(thisCol[row], row, col-1);
    			}
    		}
    		// Replace the rightmost selected column with the displaced column.
			for(int row = 0; row < height; row++) {
				con.setValueAt(oldCol[row], row, endCol-1);
			}
			//Tell the listeners that we changed the data in the table.
			con.fireTableDataChanged();
    	} else {
    		throw new Exception();
    	}
    } catch (Exception e) {
    	if(startCol < 0) {
    		JOptionPane.showMessageDialog(null, "Please select a column in order to perform that action.", "No Column Selected", JOptionPane.ERROR_MESSAGE);
    		e.printStackTrace();
    	}
    }
    } // end changeCSV
} // end class MoveColumnLeftAction
