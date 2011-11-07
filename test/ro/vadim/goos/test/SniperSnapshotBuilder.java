package ro.vadim.goos.test;

import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.SniperState;

public class SniperSnapshotBuilder {
	private SniperState state = SniperState.JOINING;
	private String itemId = "item-54321";
	private int lastPrice = 1000;
	private int lastBid = 1030;
	
	public static SniperSnapshotBuilder aSnapshot() {
		return new SniperSnapshotBuilder();
	}
	
	public SniperSnapshotBuilder withItemId(String itemId) {
		this.itemId = itemId;
		return this;
	}
	
	public SniperSnapshotBuilder withLastPrice(int lastPrice) {
		this.lastPrice = lastPrice;
		return this;
	}
	
	public SniperSnapshotBuilder withLastBid(int lastBid) {
		this.lastBid = lastBid;
		return this;
	}
	public SniperSnapshotBuilder havingState(SniperState state) {
		this.state = state;
		return this;
	}
	
	public SniperSnapshot build() {
		return new SniperSnapshot(itemId, lastPrice, lastBid, state);
	}
}
