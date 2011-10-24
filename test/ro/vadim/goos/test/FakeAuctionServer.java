package ro.vadim.goos.test;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;
import org.hamcrest.Matcher;

import ro.vadim.goos.XMPPAuction;

import static org.junit.Assert.*;

public class FakeAuctionServer {

	private final SingleMessageListener messageListener = new SingleMessageListener();
	private static final String XMPP_HOSTNAME = "Vadims-Mac.local";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_PASSWORD = "auction";
	private static final String AUCTION_RESOURCE = "Auction";
	private final String itemId;
	private final XMPPConnection connection;
	protected Chat currentChat;

	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}

	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(String.format(ITEM_ID_AS_LOGIN, itemId),
				AUCTION_PASSWORD, AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			public void chatCreated(Chat chat, boolean createdLocally) {
				currentChat = chat;
				chat.addMessageListener(messageListener);
			}
		});
	}

	public String getItemId() {
		return itemId;
	}

	public static class SingleMessageListener implements MessageListener {
		private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(
				1);

		public void processMessage(Chat chat, Message message) {
			messages.add(message);
		}

		public void receivesAMessage(Matcher<? super String> messageMatcher)
				throws InterruptedException {
			final Message message = messages.poll(5, TimeUnit.SECONDS);
			assertThat("Message", message, is(notNullValue()));
			assertThat(message.getBody(), messageMatcher);
		}
	}

	public void hasReceivedJoinRequestFromSniper(String sniperId)
			throws InterruptedException {
		receivesAMessageMatching(sniperId, equalTo(XMPPAuction.JOIN_COMMAND_FORMAT));
	}

	public void hasReceivedBid(int bid, String sniperId)
			throws InterruptedException {
		receivesAMessageMatching(sniperId,
				equalTo(String.format(XMPPAuction.BID_COMMAND_FORMAT, bid)));
	}

	public void announceClose() throws XMPPException {
		currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
	}

	public void stop() {
		connection.disconnect();
	}

	public void reportPrice(int price, int increment, String bidder)
			throws XMPPException {
		String priceMessage = String.format("SOLVersion: 1.1; Event: PRICE; "
				+ "CurrentPrice: %d; Increment: %d; Bidder: %s;", price,
				increment, bidder);
		Message message = new Message();
		message.setBody(priceMessage);
		currentChat.sendMessage(message);
	}

	private void receivesAMessageMatching(String sniperId,
			Matcher<? super String> matcher) throws InterruptedException {
		messageListener.receivesAMessage(matcher);
		assertThat(currentChat.getParticipant(), equalTo(sniperId));
	}
}
