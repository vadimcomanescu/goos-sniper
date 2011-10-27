package ro.vadim.goos;

import java.util.EventListener;

public interface SniperListener extends EventListener {

	void sniperLost();

	void sniperBidding();

	void sniperWinning();

	void sniperWon();

}
