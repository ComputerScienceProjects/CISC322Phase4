/**
 * 
 */
package editor;
import java.util.IntSummaryStatistics;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 * @author Ian Chew
 *
 */
public class ColumnStatisticsAction extends CSVAction {
    /**
     * Reports statistics to the user about the selected column.
     * If multiple columns are selected, this just reports on the first one.
     * @param con .csv content to change.
     * @param startRow The first currently selected row.
     * @param endRow The last currently selected row.
     */
	
	public ColumnStatisticsAction() {
		super("Column Statistics");
	}
	
	@Override
	protected void changeCSV(CSVContents con, int startRow, int startCol, int endRow, int endCol) {
		//List of non-blank integers in the column.
		ArrayList<Integer> colList = new ArrayList<Integer>();
		//Amount of non-blank elements in the column.
		//Is initially set to the number of elements in the column, then decremented for
		//each blank element found.
		int nonblankCount = con.getRowCount();
		
		for (int i=0; i<con.getRowCount(); i++) {
			String elem = (String)con.getValueAt(i, startCol);
			if (elem.equals("")) {
				--nonblankCount;
			} else {
				try {
					colList.add(Integer.parseInt(elem));
				} catch (NumberFormatException e) {
					//Something in the row is not an integer.
					//Thus we don't report statistics.
		    		JOptionPane.showMessageDialog(null, "To use column statistics, all elements of the selected column must be integers.",
		    				                      "Non-Numeric Input", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
		//Now colList contains all the integers in the column.
		if (nonblankCount == 0) {
			//We don't report statistics on empty columns.
    		JOptionPane.showMessageDialog(null, "To use column statistics, the column must not be empty.", "Empty Column Selected", 
    				                      JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Convert list to stream, stream to IntStream, then get statistics on that stream.
		IntSummaryStatistics stats = colList.stream().mapToInt(Integer::intValue).summaryStatistics();
		long sum=stats.getSum();
		double mean=stats.getAverage();
		int max=stats.getMax();
		int min=stats.getMin();
		JOptionPane.showMessageDialog(null, "Sum: " + sum + "\n"
				                          + "Non-blank: " + nonblankCount + "\n"
				                          + "Mean: " + mean + "\n"
				                          + "Max: " + max + "\n"
				                          + "Min: " + min + "\n");
	}
}
