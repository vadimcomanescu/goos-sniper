package ro.vadim.goos;

import javax.swing.SwingUtilities;

import ro.vadim.goos.ui.MainWindow;

public class Main {
	
	private MainWindow ui;
	
	public static final String MAIN_WINDOW_NAME = "Auction Sniper";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	
	public Main() throws Exception {
		startUserInterface();
	}
	
	
	private void startUserInterface() throws Exception{
		SwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				ui = new MainWindow();
			}
		});
		
	}

	public static void main(String... args) throws Exception {
		Main main = new Main();
	}

}
