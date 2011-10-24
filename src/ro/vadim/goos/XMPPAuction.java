package ro.vadim.goos;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;


public class XMPPAuction implements Auction {
	private final Chat chat;
	public static final String JOIN_COMMAND_FORMAT = null;
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

	public XMPPAuction(Chat chat) {
		this.chat = chat;
	}

	@Override
	public void bid(int amount) {
		try {
			chat.sendMessage(String.format(XMPPAuction.BID_COMMAND_FORMAT, amount));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void join() throws XMPPException {
		sendMessage(XMPPAuction.JOIN_COMMAND_FORMAT);
	}

	private void sendMessage(final String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}