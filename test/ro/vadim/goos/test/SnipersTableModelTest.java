package ro.vadim.goos.test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;

import com.objogate.exception.Defect;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.SniperState;
import ro.vadim.goos.ui.Column;
import ro.vadim.goos.ui.SnipersTableModel;
import static ro.vadim.goos.ui.SnipersTableModel.textFor;

@RunWith(JMock.class)
public class SnipersTableModelTest {
	private final Mockery context = new Mockery();
	private TableModelListener listener = context
			.mock(TableModelListener.class);
	private final SnipersTableModel model = new SnipersTableModel();

	@Before
	public void attachModelListener() {
		model.addTableModelListener(listener);
	}

	@Test
	public void setsUpColumnHeaders() {
		for (Column column : Column.values()) {
			assertEquals(column.name, model.getColumnName(column.ordinal()));
		}
	}

	@Test
	public void hasEnoughColumns() {
		assertEquals(model.getColumnCount(), Column.values().length);
	}

	@Test
	public void setsSniperValuesInColumns() {
		SniperSnapshot joining = SniperSnapshot.joining("item123");
		SniperSnapshot bidding = joining.bidding(555, 667);
		context.checking(new Expectations() {
			{
				allowing(listener).tableChanged(with(anyInsertionEvent()));
				oneOf(listener).tableChanged(with(aChangeInRow(0)));
			}

		});

		model.addSniper(joining);
		model.sniperStateChanged(bidding);
		assertRowMatchesSnapshot(0, bidding);
	}

	@Test
	public void notifiesListenersWhenAddingASniper() {
		SniperSnapshot joining = SniperSnapshot.joining("item123");

		context.checking(new Expectations() {
			{
				one(listener).tableChanged(with(anInsertionAtRow(0)));
			}
		});
		assertEquals(0, model.getRowCount());
		model.addSniper(joining);
		assertEquals(1, model.getRowCount());
		assertRowMatchesSnapshot(0, joining);
	}

	@Test
	public void holdsSnipersInAdditionOrder() {
		context.checking(new Expectations() {
			{
				ignoring(listener);
			}
		});
		model.addSniper(SniperSnapshot.joining("item 0"));
		model.addSniper(SniperSnapshot.joining("item 1"));
		assertEquals("item 0", getValueAt(0, Column.ITEM_IDENTIFIER));
		assertEquals("item 1", getValueAt(1, Column.ITEM_IDENTIFIER));
	}

	@Test(expected = Defect.class)
	public void raisesExceptionWhenSnapshotChangedForInexistentItem() {
		context.checking(new Expectations() {
			{
				ignoring(listener);
			}
		});
		
		model.addSniper(SniperSnapshot.joining("item 0"));

		SniperSnapshot snapshotForAnotherItem = SniperSnapshotBuilder
				.aSnapshot().withItemId("item 1").build();

		model.sniperStateChanged(snapshotForAnotherItem);
	}

	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
		assertEquals(snapshot.itemId, getValueAt(row, Column.ITEM_IDENTIFIER));
		assertEquals(snapshot.lastBid, getValueAt(row, Column.LAST_BID));
		assertEquals(snapshot.lastPrice, getValueAt(row, Column.LAST_PRICE));
		assertEquals(textFor(snapshot.state),
				getValueAt(row, Column.SNIPER_STATE));
	}

	private Object getValueAt(final int rowIndex, Column column) {
		return model.getValueAt(rowIndex, column.ordinal());
	}

	private Matcher<TableModelEvent> anInsertionAtRow(final int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row, row,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	private Matcher<TableModelEvent> aChangeInRow(final int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row, row,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
	}

	private Matcher<TableModelEvent> anyInsertionEvent() {
		return hasProperty("type", equalTo(TableModelEvent.INSERT));
	};
}
