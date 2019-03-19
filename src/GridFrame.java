package cavegenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/*
 * Grid Frame
 * 
 * A frame which contains a grid panel and is used as a view for the cellular automaton.
 * Uses CAController as its controller (Model-View-Controller Pattern).
 * Contains a 100x100 grid of 10,000 cells (each one 4x4 pixels in size).
 * Width of frame = 400 pixels, Height of frame = 425 pixels.
 * 
 * The view will communicate with the controller automatically with a timer.
 * So, it advances the cave generation on its own without any need for user input.
 * 
 * Input:
 * MOUSE CLICK -> RESTART THE CAVE GENERATION PROCESS.
 */

public class GridFrame extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private GridPanel gridPanel;
	private Timer timer;
	
	private CAController controller;
	
	/*
	 * Grid Frame Constructor: constructs new GridFrame object.
	 * Contains a 100x100 grid of 10,000 cells (each one 4x4 pixels in size).
	 * Width of frame = 400 pixels, Height of frame = 425 pixels.
	 */
	public GridFrame() {
		setTitle("Cave Level Generator (Click to Restart)");
		gridPanel = new GridPanel(100, 100, 4, Color.BLACK);
		this.getContentPane().setBackground(Color.BLACK);
		setMinimumSize(new Dimension(400, 425));
		setPreferredSize(new Dimension(400, 425));
		setResizable(false);
		add(gridPanel);
		addMouseListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * Sets the CAController object.
	 * Sets up a timer object to automatically communicate with CAController.
	 */
	public void setCAController(CAController controller) {
		if(timer != null)
			timer.stop();
		this.controller = controller;
		// timer set to 1/4 of a second
		timer = new Timer(250, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                controller.doCaveGenAlgorithm();             
            }
     });
	 timer.start();
	}
	
	/*
	 * Returns gridPanel, a GridPanel object which stores a graphical representation of the cellular automaton the frame is viewing.
	 */
	public GridPanel getGridPanel() {
		return gridPanel;
	}

	/*
	 * When the mouse is clicked, tell the controller to restart cave generation.
	 * Also, restart the timer in our view that advances the cave generation automatically.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		timer.restart();
		controller.doRestart();
	}

	// The following methods are not currently used for input:
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
		
}
