package ro.vadim.goos.ui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame{
	
	public static final String MAIN_WINDOW_NAME = "Auction Sniper";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	public static final String STATUS_LOST = "losing";
	private static final String STATUS_JOINING = "joining";
	private static final JLabel sniperStatus = createLabel(STATUS_JOINING);
	

	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		add(sniperStatus);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private static JLabel createLabel(String initialText) {
		JLabel result = new JLabel(initialText);
		result.setName(SNIPER_STATUS_NAME);
		result.setBorder(new LineBorder(Color.black));
		return result;
	}

	public void showStatus(String status) {
		sniperStatus.setText(status);
	}
}
