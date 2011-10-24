package ro.vadim.goos;

public interface AuctionEventListener {
	
	public void auctionClosed();

	public void currentPrice(int price, int increment);
}
