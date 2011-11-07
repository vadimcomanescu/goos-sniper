package ro.vadim.goos;

public class AuctionSniper implements AuctionEventListener {
	private SniperListener sniperListener;
	private Auction auction;
	private SniperSnapshot sniperSnapshot;

	public AuctionSniper(Auction auction, SniperListener sniperListener,
			String itemId) {
		this.sniperListener = sniperListener;
		this.auction = auction;
		this.sniperSnapshot = SniperSnapshot.joining(itemId);
	}

	@Override
	public void auctionClosed() {
		sniperSnapshot = sniperSnapshot.closed();
		notifyChange();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		switch (priceSource) {
			case FromSniper :
				sniperSnapshot = sniperSnapshot.winning(price);
				break;
			case FromOtherBidder :
				final int bid = price + increment;
				auction.bid(bid);
				sniperSnapshot = sniperSnapshot.bidding(price, bid);
				break;
		}
		notifyChange();
	}

	private void notifyChange() {
		sniperListener.sniperStateChanged(sniperSnapshot);
	}
}
