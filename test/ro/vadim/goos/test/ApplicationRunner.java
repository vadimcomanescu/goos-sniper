package ro.vadim.goos.test;

import ro.vadim.goos.Main;
import ro.vadim.goos.ui.MainWindow;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = "sniper@vadims-mac.local/Auction";
	protected static final String XMPP_HOSTNAME = "localhost";
	private AuctionSniperDriver driver;
	private String itemId;

	public void startBiddingIn(final FakeAuctionServer auction) {
		itemId = auction.getItemId();
		Thread thread = new Thread("Test Application") {
			@Override
			public void run() {
				try {
					Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD,
							auction.getItemId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(itemId, 0, 0, MainWindow.STATUS_JOINING);
	}

	public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_LOST);
	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning(int winningBid) {
		driver.showsSniperStatus(itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
	}
	
	public void showsSniperHasWonAuction(int lastPrice) {
		driver.showsSniperStatus(itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
