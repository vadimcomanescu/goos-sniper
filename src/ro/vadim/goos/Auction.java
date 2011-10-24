package ro.vadim.goos;

import org.jivesoftware.smack.XMPPException;

public interface Auction {

	public void bid(int amount);
	
	public void join() throws XMPPException;

}
