package ro.vadim.goos.test;
import static org.hamcrest.Matchers.*;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.vadim.goos.Auction;
import ro.vadim.goos.AuctionEventListener.PriceSource;
import ro.vadim.goos.AuctionSniper;
import ro.vadim.goos.SniperListener;
import ro.vadim.goos.SniperSnapshot;
import ro.vadim.goos.SniperState;

@RunWith(JMock.class)
public class AuctionSniperTest {
	protected static final String ITEM_ID = "item-54321";
	private final Mockery context = new Mockery();
	private final Auction auction = context.mock(Auction.class);
	private final SniperListener sniperListener = context
			.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(auction,
			sniperListener, ITEM_ID);
	private final States sniperState = context.states("sniper");

	private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
		return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state),
				"sniper that is ", "was") {
			@Override
			protected SniperState featureValueOf(SniperSnapshot actual) {
				return actual.state;
			}
		};
	}
	@Test
	public void reportsLostWhenAuctionClosesImmediately() {
		context.checking(new Expectations() {
			{
				oneOf(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.LOST)));
			};
		});

		sniper.auctionClosed();
	}

	@Test
	public void reportsLostWhenAuctionClosesWhileBidding() {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.BIDDING)));
				then(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.LOST)));
				when(sniperState.is("bidding"));
			}

		});

		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}

	@Test
	public void reportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.WINNING)));
				then(sniperState.is("winning"));
				atLeast(1).of(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.WON)));
				when(sniperState.is("winning"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}

	// TO DO : FIx this test!!
	// @Test
	// public void reportsBiddingWhenItsOutbidWhileWinning() {
	// final int price = 123;
	// final int increment = 45;
	// final int bid = price + increment;
	// context.checking(new Expectations() {
	// {
	// ignoring(auction);
	// allowing(sniperListener).sniperWinning();
	// then(sniperState.is("winning"));
	// oneOf(auction).bid(bid);
	// atLeast(1).of(sniperListener).sniperBidding(new SniperState(ITEM_ID,
	// price, bid));
	// when(sniperState.is("winning"));
	// // then(sniperState.is("bidding"));
	// }
	// });
	// sniper.currentPrice(price, increment, PriceSource.FromSniper);
	// sniper.currentPrice(bid, increment, PriceSource.FromOtherBidder);
	// }

	@Test
	public void reportsWinningWhenCurrentPriceComesFromSniper() {

		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.BIDDING)));
				then(sniperState.is("bidding"));
				SniperSnapshot snapshot = SniperSnapshotBuilder.aSnapshot()
						.withItemId(ITEM_ID).withLastBid(135)
						.withLastPrice(135).havingState(SniperState.WINNING)
						.build();
				atLeast(1).of(sniperListener).sniperStateChanged(snapshot);
				when(sniperState.is("bidding"));
			}
		});
		sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
		sniper.currentPrice(135, 45, PriceSource.FromSniper);
	}

	@Test
	public void biddsHigherAndReportsBiddingWhenNewPriceArrives() {
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;
		context.checking(new Expectations() {
			{
				oneOf(auction).bid(bid);
				SniperSnapshot snapshot = SniperSnapshotBuilder.aSnapshot()
						.withItemId(ITEM_ID).withLastBid(bid)
						.withLastPrice(price).havingState(SniperState.BIDDING)
						.build();
				atLeast(1).of(sniperListener).sniperStateChanged(snapshot);
			};
		});

		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
}
