package ro.vadim.goos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ro.vadim.goos.ui.MainWindow;
import ro.vadim.goos.ui.SnipersTableModel;

public class Main {

	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
	private final SnipersTableModel snipers = new SnipersTableModel();
	private MainWindow ui;
	private Vector<Chat> noToBeGcd = new Vector<Chat>();

	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				ui = new MainWindow(snipers);
			}
		});
	}

	public static void main(String... args) throws Exception {
		Main main = new Main();
		XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(connection);

		for (int i = 3; i < args.length; i++) {
			main.joinAuction(connection, args[i]);
		}
	}

	private void joinAuction(XMPPConnection connection, String itemId) throws Exception {
		safelyAddItemToModel(itemId);
		final Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
		noToBeGcd.add(chat);

		Auction auction = new XMPPAuction(chat);
		chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(), new AuctionSniper(auction,
				new SwingThreadSniperListener(snipers), itemId)));
		auction.join();
	}

	private void safelyAddItemToModel(final String itemId) throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				snipers.addSniper(SniperSnapshot.joining(itemId));
			}
		});
	}

	private void disconnectWhenUICloses(final XMPPConnection connection) {
		ui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				connection.disconnect();
			}
		});
	}

	public class SwingThreadSniperListener implements SniperListener {

		private SnipersTableModel snipers;

		public SwingThreadSniperListener(SnipersTableModel snipers) {
			this.snipers = snipers;
		}

		@Override
		public void sniperStateChanged(final SniperSnapshot sniperSnapshot) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					snipers.sniperStateChanged(sniperSnapshot);
				}
			});
		}
	}

	private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		return connection;
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}
}
