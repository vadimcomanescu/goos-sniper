package ro.vadim.goos.test;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.vadim.goos.Auction;
import ro.vadim.goos.AuctionSniper;
import ro.vadim.goos.SniperListener;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private final Mockery context = new Mockery();
	private final Auction auction = context.mock(Auction.class);
	private final SniperListener sniperListener = context
			.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);

	@Test
	public void reportsLostWhenAuctionClosed() {
		context.checking(new Expectations() {
			{
				oneOf(sniperListener).sniperLost();
			};
		});

		sniper.auctionClosed();
	}

	@Test
	public void biddsHigherWhenNewPriceArrives() {
		final int price = 1001;
		final int increment = 25;
		context.checking(new Expectations() {
			{
				oneOf(auction).bid(price + increment);
				atLeast(1).of(sniperListener).snipperBidding();
			};
		});
		
		sniper.currentPrice(price, increment);
	}

}
