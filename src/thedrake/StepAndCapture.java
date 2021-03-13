package thedrake;

import thedrake.model.BoardPos;

public class StepAndCapture extends BoardMove {

	public StepAndCapture(BoardPos origin, BoardPos target) {
		super(origin, target);
	}

	@Override
	public GameState execute(GameState originState) {
		return originState.stepAndCapture(origin(), target());
	}
	
}
