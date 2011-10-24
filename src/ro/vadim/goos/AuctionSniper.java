package ro.vadim.goos;

public class AuctionSniper implements AuctionEventListener{
	private SniperListener sniperListener;
	private Auction auction;
	
	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.sniperListener = sniperListener;
		this.auction = auction;
	}

	@Override
	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int price, int increment) {
		auction.bid(price + increment);
		sniperListener.snipperBidding();
	}

}
