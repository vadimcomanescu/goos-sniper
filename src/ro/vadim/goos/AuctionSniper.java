package ro.vadim.goos;

public class AuctionSniper implements AuctionEventListener {
	private SniperListener sniperListener;
	private Auction auction;
	private boolean isWinning;
	private SniperSnapshot sniperSnapshot;

	public AuctionSniper(Auction auction, SniperListener sniperListener,
			String itemId) {
		this.sniperListener = sniperListener;
		this.auction = auction;
		this.sniperSnapshot = SniperSnapshot.joining(itemId);
	}

	@Override
	public void auctionClosed() {
		if (isWinning) {
			sniperListener.sniperWon();
		} else {
			sniperListener.sniperLost();
		}
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		isWinning = priceSource == PriceSource.FromSniper;
		if (isWinning) {
			sniperSnapshot = sniperSnapshot.winning(price);
		} else {
			final int bid = price + increment;
			auction.bid(bid);
			sniperSnapshot = sniperSnapshot.bidding(price, bid);
		}
		
		sniperListener.sniperStateChanged(sniperSnapshot);
	}
}
