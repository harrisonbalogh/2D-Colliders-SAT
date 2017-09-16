package ui;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import readers.HXKey;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Color;

import world.user_interface.HXWorldPanel;

public class HXMasterWindow extends JFrame {
	
	// The dimensions of the window
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	// The dimensions of the world inside the world panel
	public static final int WORLD_WIDTH = 1000;
	public static final int WORLD_HEIGHT = 500;
	
	/**
	 * The primary window for the application.
	 */
	public HXMasterWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the window. 
	 * <p>
	 * This includes attributes of the window and its components
	 * as well as any keys the window should listen to.
	 */
	private void initialize() {
		// Attributes of the parent window
		getContentPane().setBackground(Color.WHITE);
		setTitle("2D Physics Environment Test");
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		// The viewpanel that renders the 2D world
		HXWorldPanel viewPanel = new HXWorldPanel(WORLD_WIDTH, WORLD_HEIGHT);
		getContentPane().add(viewPanel);
		
		// === Initialize key bindings for keys listed in the HXKey.KEYS HashMap ===
		InputMap in = viewPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = viewPanel.getActionMap();
		// Iterate through keys in HXKey.KEYS HashMap
		for (String key : HXKey.KEYS.keySet()) {
			// Put actionMapKey into inputmap and actionmap for pressed
			in.put(KeyStroke.getKeyStroke(key.toUpperCase()), "do_" + key + "_pressed");
			am.put("do_" + key + "_pressed", new AbstractAction() {
				// Add method for key pressed down, letting isPressed() in HXKey return true
				@Override
				public void actionPerformed(ActionEvent e) {
					HXKey.KEYS.put(key, true);
				}
			});
			// Put actionMapKey into inputmap and actionmap for released
			in.put(KeyStroke.getKeyStroke("released " + key.toUpperCase()), "do_" + key + "_released");
			am.put("do_" + key + "_released", new AbstractAction(){
				// Add method for key released up, letting isPressed() in HXKey return false
				@Override
				public void actionPerformed(ActionEvent e) {
					HXKey.KEYS.put(key, false);
				}
			});
		}
	}
}
