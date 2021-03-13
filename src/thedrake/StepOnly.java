package thedrake;

import thedrake.model.BoardPos;

public class StepOnly extends BoardMove {

	public StepOnly(BoardPos origin, BoardPos target) {
		super(origin, target);
	}

	@Override
	public GameState execute(GameState originState) {
		return originState.stepOnly(origin(), target());
	}
	
}
