package ro.vadim.goos.ui;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	
	private static final String MAIN_WINDOW_NAME = "Auction Sniper";

	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
