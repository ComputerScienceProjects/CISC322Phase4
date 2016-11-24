// $Id: TextAction.java,v 1.1 2012/10/24 17:06:40 dalamb Exp $
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
 * Copyright 2010-2011 David Alex Lamb.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public abstract class CSVAction extends DefaultAction {
    /**
     * Constructs a csv manipulation action
     */
    private CSVAction() {
	super("Text");
    } // end constructor TextAction

    /**
     * Constructs a csv manipulation action
     * @param name Name of the action.
     */
    protected CSVAction(String name) {
	super(name);
    } // end constructor TextAction
    
    /**
     * Perform some appropriate change on a selected region of the table;
     *    subclasses must implement this method. If
     * <code>start</code> and <code>end</code> are equal, the operation might
     * do nothing (as in capitalization) or might affect the character before
     * the start or after the end (as in using a delete or backspace key).
     * @param con Text document to change.
     * @param start Index of the first character to change.
     * @param end Index one beyond the last character to change.
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
	    // if (firstArea==null) setArea(area);
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
    }

    // debugging
    /*
    private static JTextArea firstArea = null;
    private static void setArea(JTextArea area) {
	TextType.setActions(area);
	Keymap km = area.getKeymap();
	if (km==null) {System.out.println("No keymap"); return; }
	String actionName=DefaultEditorKit.pasteAction;
	Action ac = TextType.getNamedAction(actionName);
	TextType.debugAction(actionName, km, ac);
	debugStroke("ctrl pressed V",km);
	debugStroke("ctrl X",km);
	debugStroke("ctrl pressed C",km);
	firstArea = area;
    }
    private static void debugStroke(String stroke, Keymap km) {
	KeyStroke testChar = KeyStroke.getKeyStroke(stroke);
	if (testChar!=null) {
	    System.out.print("test='"+stroke+"' '"+testChar+"'");
	    while (km != null) {
		if (km.isLocallyDefined(testChar)) {
		    Action ac = km.getAction(testChar);
		    if (ac==null) System.out.print(" no action");
		    else System.out.print(" action "+ac);
		    break;
		} else {
		    System.out.println(" not in "+km);
		    km = km.getResolveParent();
		}
	    } // while 
	    System.out.println();
	} else System.out.println("No ctl-C keystroke");
    } // debugStroke
    */
} // end class CSVAction
