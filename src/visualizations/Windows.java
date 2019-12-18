package visualizations;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Windows extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLayeredPane pane = getLayeredPane();

	private JFrame gf = new JFrame();

	public void runWindow(String title) {
		gf.setVisible(true);
		gf.setSize(800, 800);
		gf.setTitle(title);
		gf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gf.setVisible(true);
		gf.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EXIT");
		gf.getRootPane().getActionMap().put("EXIT", new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				gf.dispose();
			}
		});

	}

	public void addClusptVis(ClusptVisualize clusptVis) {
		gf.add(clusptVis);
		gf.setVisible(true);

	}

}
