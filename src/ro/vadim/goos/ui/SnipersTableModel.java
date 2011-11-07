package ro.vadim.goos.ui;

import javax.swing.table.AbstractTableModel;

import ro.vadim.goos.SniperListener;
import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.SniperState;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel implements SniperListener{
	private final static String[] STATUS_TEXT = {"Joining", "Bidding",
			"Winning", "Lost", "Won"};
	
	private final static SniperSnapshot STARTING_UP = new SniperSnapshot(
			"item-54321", 0, 0, SniperState.JOINING);
	private SniperSnapshot sniperSnapshot = STARTING_UP;

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(sniperSnapshot);
	}
	
	@Override
	public String getColumnName(int column) {
		return Column.at(column).name;
	}

	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

	public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
		this.sniperSnapshot = newSniperSnapshot;
		fireTableRowsUpdated(0, 0);
	}
}
