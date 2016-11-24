package editor;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ca.queensu.cs.dal.edfmwk.Application;
import ca.queensu.cs.dal.edfmwk.win.CommonWindow;
import ca.queensu.cs.dal.flex.log.Log;

/**
* {@link javax.swing.Action} removes any current sort.
 *<p>
 * Copyright 2016 CISC 322 Group 7.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 * 
 * @author CISC 322 Group 7
 * @version 1.0
 */

public class UnsortAction extends CSVAction {

	/**
     * Constructs a unsort action -- removes the current sort on the jTable.
     */
	public UnsortAction() {
		super("Unsort");
	} //end constructor ColumnStatisticsAction
	
	/**
	 * Stub, since we're implementing all the functionality by overriding actionPerformed.
	 */
	@Override
	protected void changeCSV(CSVContents con, int startRow, int startCol, int endRow, int endCol) {} // stub
	
	@Override
	/**
	 * Resets the sort of the jTable.
	 */
    public void actionPerformed(ActionEvent ae) {
	try {
	    Application app = Application.getApplication();
	    CommonWindow win = app.getActiveWindow();
	    JTable table = (JTable) ((JScrollPane) win.getContentPane()).getViewport().getView();
	    if (table.getRowSorter().getSortKeys().isEmpty()) {
    		JOptionPane.showMessageDialog(table, "The table is already unsorted.",
                    "Table Already Unsorted", JOptionPane.ERROR_MESSAGE);
	    } else {
		    table.getRowSorter().setSortKeys(null); //Reset the sort.	    	
	    }
	} catch (Exception ex) {
	    Log.error("CSV action error: "+ex.getLocalizedMessage());
	}
	} // end actionPerformed
}