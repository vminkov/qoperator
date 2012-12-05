package net.virtualqueues.qoperator.view;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class QOpGUI implements Runnable {
	JFrame mainWindow;
	
	@Override
	public void run() {
		mainWindow = new JFrame();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//dispose_on_close to leave the rest stuff running
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
		mainWindow.add(new MainPanel());
		mainWindow.setVisible(true);
	
	}

}
