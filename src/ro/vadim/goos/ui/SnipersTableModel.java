package ro.vadim.goos.ui;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel {
	private String statusText = MainWindow.STATUS_JOINING;

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return statusText;
	}

	public void setStatusText(String newStatus) {
		statusText = newStatus;
		fireTableRowsUpdated(0, 0);
	}

}
