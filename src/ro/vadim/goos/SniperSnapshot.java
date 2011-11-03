package ro.vadim.goos;


public class SniperSnapshot {
	public final String itemId;
	public final int lastPrice;
	public final int lastBid;
	public SniperState state;

	public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState sniperState) {
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
		this.state = sniperState;
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof SniperSnapshot))
			return false;

		return itemId.equals(((SniperSnapshot) other).itemId)
				&& lastPrice == ((SniperSnapshot) other).lastPrice
				&& lastBid == ((SniperSnapshot) other).lastBid;
	}

	public String toString() {
		return String.format("Item: %s, last price: %d, last bid: %d", itemId,
				lastPrice, lastBid);
	}

	public static SniperSnapshot joining(String itemId) {
		return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
	}

	public SniperSnapshot winning(int price) {
		return new SniperSnapshot(this.itemId, price, this.lastBid, SniperState.WINNING);
	}

	public SniperSnapshot bidding(int price, int bid) {
		return new SniperSnapshot(this.itemId, price, bid, SniperState.BIDDING);
	}
}
