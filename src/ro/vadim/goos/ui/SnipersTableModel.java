package ro.vadim.goos.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.objogate.exception.Defect;

import ro.vadim.goos.SniperListener;
import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.SniperState;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel
		implements
			SniperListener {
	private final static String[] STATUS_TEXT = {"Joining", "Bidding",
			"Winning", "Lost", "Won"};

	private ArrayList<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public int getRowCount() {
		return snapshots.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		SniperSnapshot snapshot = snapshots.get(rowIndex);
		return Column.at(columnIndex).valueIn(snapshot);
	}

	@Override
	public String getColumnName(int column) {
		return Column.at(column).name;
	}

	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

	public void sniperStateChanged(SniperSnapshot snapshot) {
		int rowOfTheSniperThatChanged = rowOfTheSniperThatChanged(snapshot);
		snapshots.set(rowOfTheSniperThatChanged, snapshot);
		fireTableRowsUpdated(rowOfTheSniperThatChanged,
				rowOfTheSniperThatChanged);
	}

	private int rowOfTheSniperThatChanged(SniperSnapshot snapshot) {
		for (int rowNumber = 0; rowNumber < snapshots.size(); rowNumber++) {
			if (snapshots.get(rowNumber).isForTheSameItemAs(snapshot)) {
				return rowNumber;
			}
		}
		throw new Defect("Cannot find any previous snapshot for "
				+ snapshot.itemId);
	}

	public void addSniper(SniperSnapshot snapshot) {
		snapshots.add(snapshot);
		final int lastInsertedRow = snapshots.size() - 1;
		fireTableRowsInserted(lastInsertedRow, lastInsertedRow);
	}
}
