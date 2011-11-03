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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.SniperState;
import ro.vadim.goos.ui.MainWindow;
import ro.vadim.goos.ui.SnipersTableModel;
import ro.vadim.goos.ui.SnipersTableModel.Column;

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
	public void hasEnoughColumns() {
		assertEquals(model.getColumnCount(), Column.values().length);
	}

	@Test
	public void setsSniperValuesInColumns() {
		context.checking(new Expectations() {
			{
				oneOf(listener).tableChanged(with(aRowChangedEvent()));
			};
		});

		model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));
		assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
		assertColumnEquals(Column.LAST_PRICE, 555);
		assertColumnEquals(Column.LAST_BID, 666);
		assertColumnEquals(Column.SNIPER_STATE, MainWindow.STATUS_BIDDING);
	}

	private void assertColumnEquals(Column column, Object expected) {
		final int rowIndex = 0;
		final int columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}
	private Matcher<TableModelEvent> aRowChangedEvent() {
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}
}
