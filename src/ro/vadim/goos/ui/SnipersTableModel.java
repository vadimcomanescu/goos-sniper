package ro.vadim.goos.ui;

import javax.swing.table.AbstractTableModel;

import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.SniperState;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel {
	private static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING };
	
	private final static SniperSnapshot STARTING_UP = new SniperSnapshot("item-54321", 0, 0, SniperState.JOINING);
	private SniperSnapshot sniperSnapshot = STARTING_UP;
	private String state = MainWindow.STATUS_JOINING;

	public enum Column {
		ITEM_IDENTIFIER, LAST_PRICE, LAST_BID, SNIPER_STATE;
		public static Column at(int offset) {
			return values()[offset];
		}
	}

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
		switch (Column.at(columnIndex)) {
			case ITEM_IDENTIFIER :
				return sniperSnapshot.itemId;
			case LAST_PRICE :
				return sniperSnapshot.lastPrice;
			case LAST_BID :
				return sniperSnapshot.lastBid;
			case SNIPER_STATE :
				return state;
			default :
				throw new IllegalArgumentException("No column at "
						+ columnIndex);
		}
	}

	public void setStatusText(String newStatus) {
		this.state = newStatus;
		fireTableRowsUpdated(0, 0);
	}

	public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
		this.sniperSnapshot = newSniperSnapshot;
		this.state = STATUS_TEXT[newSniperSnapshot.state.ordinal()]; 
		fireTableRowsUpdated(0, 0);
	}
}
