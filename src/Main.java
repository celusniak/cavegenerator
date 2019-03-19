package cavegenerator;

/*
 * Main: sets up the demonstration of cave generation using cellular automata
 */

public class Main {

	/*
	 * main: sets up the demonstration of cave generation using cellular automata
	 */
	public static void main(String[] a) {
		GridFrame view = new GridFrame();
		CellularAutomaton cellularAutomaton = new CellularAutomaton(100, 100);
		CAController controller = new CAController();
		controller.setCellularAutomaton(cellularAutomaton);
		controller.setGridFrame(view);
		view.setCAController(controller);
		view.setVisible(true);
	}
	
}
