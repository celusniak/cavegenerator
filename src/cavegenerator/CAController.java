package cavegenerator;

import java.awt.Color;
import java.awt.Point;

/*
 * CA Controller
 * 
 * Used to control a Cellular Automaton (Model-View-Controller pattern).
 * 
 * Call doCaveGenAlgorithm() to advance the cave generation.
 * 
 * Cave generation:
 * 1. Start with walls
 * 2. Fill with noise (using chanceOfWall as a parameter)
 * 3. Apply rules (applyRulesNumIterations number of times)
 * 4. Cull smaller caverns
 * 5. If the cave system is not the desired size, go back to 1 (Start with walls)
 * 6. Otherwise, continue on to 7
 * 7. Finish (wait for doRestart() to be called)
 * *If at any time doRestart() is called during the cave generation, then the state will be reset to 1 (Start with walls)
 * 
 */

public class CAController {
	
	public enum State{START_WITH_WALLS, FILL_WITH_NOISE, APPLY_RULES, CULL_SMALLER_CAVERNS, SIZE_CONTROL, FINISH};
	
	private State state = State.START_WITH_WALLS; // which state of generation the controller is currently at
	
	private GridFrame gridFrame; // view
	private CellularAutomaton automaton; // the cellular automaton model being controlled
	
	private double chanceOfWall = 0.5; // chance of wall during when it fills the cellular automaton's cell grid with noise
	
	private int applyRulesNumIterations = 20; // number of times the controller tells the cellular automaton to applyRules()
	private int applyRulesIteration = 0; // which iteration the controller is currently on
	
	private int minimumSize = 1500; // the minimum desired size of the generated cave system
	
	/*
	 * Advances the cave generation:
	 * 1. Start with walls
	 * 2. Fill with noise (using chanceOfWall as a parameter)
	 * 3. Apply rules (applyRulesNumIterations number of times)
	 * 4. Cull smaller caverns
	 * 5. If the cave system is not the desired size, go back to 1 (Start with walls)
	 * 6. Otherwise, continue on to 7
	 * 7. Finish (wait for doRestart() to be called)
	 * *If at any time doRestart() is called during the cave generation, then the state will be reset to 1 (Start with walls)
	 */
	public void doCaveGenAlgorithm() {
		// 1. Start with walls.
		if(state == State.START_WITH_WALLS) {
			applyRulesIteration = 0;
			automaton.fillCellGridWithWalls();
			state = State.FILL_WITH_NOISE;
			doUpdateView();
		}
		// 2. Fill with noise.
		else if(state == State.FILL_WITH_NOISE) {
			automaton.fillCellGridWithNoise(chanceOfWall);
			state = State.APPLY_RULES;
			doUpdateView();
		}
		// 3. Apply rules (applyRulesNumIterations number of times).
		else if(state == State.APPLY_RULES) {
			automaton.applyRules();
			if(applyRulesIteration >= applyRulesNumIterations) {
				state = State.CULL_SMALLER_CAVERNS;
				applyRulesIteration = 0;
			}
			else {
				applyRulesIteration++;
			}
			doUpdateView();
		}
		// 4. Cull smaller caverns.
		else if(state == State.CULL_SMALLER_CAVERNS) {
			automaton.cullSmallerCaverns();
			state = State.SIZE_CONTROL;
			doUpdateView();
		}
		// 5. If the cave system is not the desired size, go back to 1 (Start with walls).
		else if(state == State.SIZE_CONTROL) {
			if(automaton.findArea() >= minimumSize) {
				state = State.FINISH;
			}
			else {
				state = State.START_WITH_WALLS;
			}
			doUpdateView();
		}
		// 7. Finish (wait for doRestart() to be called).
	}
	
	
	/*
	 * Sets the CellularAutomaton object.
	 */
	public void setCellularAutomaton(CellularAutomaton automaton) {
		this.automaton = automaton;
	}
	
	
	/*
	 * Sets the GridFrame object.
	 * Also, updates the data displayed in the frame when it does this.
	 */
	public void setGridFrame(GridFrame gridFrame) {
		this.gridFrame = gridFrame;
		doUpdateView();
	}

	
	/*
	 * Updates the frame with the data from the cell grid in automaton model.
	 */
	public void doUpdateView() {
		if(automaton != null) {
			for(int x = 0; x < automaton.getWidth(); x++) {
				for(int y = 0; y < automaton.getHeight(); y++) {
					boolean state = automaton.getStateOfCell(new Point(x, y));
					if(state == CellularAutomaton.WALL) {
						gridFrame.getGridPanel().setCellBorderColor(x, y, Color.BLACK);
						gridFrame.getGridPanel().setCellColor(x, y, Color.BLACK);
					}
					else {
						gridFrame.getGridPanel().setCellBorderColor(x, y, Color.GRAY);
						gridFrame.getGridPanel().setCellColor(x, y, Color.WHITE);					
					}
				}
			}
		}
		gridFrame.getContentPane().repaint();
	}
	
	
	/*
	 * Restarts the cave generation.
	 * *If at any time doRestart() is called during the cave generation, then the state will be reset to 1 (Start with walls)
	 */
	public void doRestart() {
		state = State.START_WITH_WALLS;
	}
	
	
}
