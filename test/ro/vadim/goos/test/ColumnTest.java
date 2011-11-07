package ro.vadim.goos.test;

import static org.junit.Assert.*;
import org.junit.Test;
import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.ui.Column;
import ro.vadim.goos.ui.SnipersTableModel;

public class ColumnTest {

	private SniperSnapshot snapshot = SniperSnapshotBuilder.aSnapshot().build();

	@Test
	public void valueInTheItemIdentifierColumnIsForTheSnapshotItemIdentifier() {
		assertEquals(snapshot.itemId, Column.ITEM_IDENTIFIER.valueIn(snapshot));
	}

	@Test
	public void valueInTheLastPriceColumnIsForTheLastPriceOfASnapshot() {
		assertEquals(snapshot.lastPrice, Column.LAST_PRICE.valueIn(snapshot));
	}

	@Test
	public void valueInTheLastBidColumnIsForTheLastBidOfASnapshot() {
		assertEquals(snapshot.lastBid, Column.LAST_BID.valueIn(snapshot));
	}

	@Test
	public void valueInTheStateColumnIsForTheTextualRepresentationOfASnapshotState() {
		assertEquals(SnipersTableModel.textFor(snapshot.state),
				Column.SNIPER_STATE.valueIn(snapshot));
	}
}
