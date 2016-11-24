package editor;
import java.awt.event.ActionEvent;
// import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;
// import javax.swing.text.Keymap;
// import javax.swing.text.DefaultEditorKit;
// import javax.swing.KeyStroke;
import ca.queensu.cs.dal.edfmwk.Application;
import ca.queensu.cs.dal.edfmwk.act.DefaultAction;
import ca.queensu.cs.dal.edfmwk.win.CommonWindow;
import ca.queensu.cs.dal.flex.log.Log;

/**
 * Parent for {@link javax.swing.Action Actions} for implementing changes to
 * the current table selection. Subclasses need only implement the
 * {@link #changeCSV} method.
 *<p>
 * Copyright 2016 CISC 322 Group 7.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public abstract class CSVAction extends DefaultAction {
    /**
     * Constructs a csv manipulation action
     */
    private CSVAction() {
	super("Text");
    } // end constructor CSVAction

    /**
     * Constructs a csv manipulation action
     * @param name Name of the action.
     */
    protected CSVAction(String name) {
	super(name);
    } // end constructor CSVAction
    
    /**
     * Perform some appropriate change on a selected region of the table;
     * subclasses must implement this method. Note that some actions
     * might not use all this information. For example, generating statistics on
     * the currently selected column will likely only care about <code>startCol</code>.
     * 
     * @param con csv table to change.
     * @param startRow Index of the first row to change.
     * @param endRow Index of the last row to change.
     * @param startCol Index of the first column to change.
     * @param endCol Index of the last column to change.
     */
    protected abstract void changeCSV(CSVContents con, int startRow, int startCol, int endRow, int endCol);

    /**
     * Perform the appropriate action (defined by {@link #changeCSV}) on the
     *    currently-selected region of the document.
     */
    public void actionPerformed(ActionEvent ae) {
	try {
	    Application app = Application.getApplication();
	    CommonWindow win = app.getActiveWindow();
	    JTable table = (JTable) ((JScrollPane) win.getContentPane()).getViewport().getView();
	    CSVDocument doc = (CSVDocument) app.getActiveDocument();
	    CSVContents con = doc.getContents();
	    int startRow = table.getSelectedRow();
	    int startCol = table.getSelectedColumn();
	    int endRow   = startRow + table.getSelectedRowCount();
	    int endCol   = startCol + table.getSelectedColumnCount();
	    changeCSV(con,startRow,startCol,endRow,endCol);
	} catch (Exception ex) {
	    Log.error("CSV action error: "+ex.getLocalizedMessage());
	}
    } // end actionPerformed
	
} // end class CSVAction
