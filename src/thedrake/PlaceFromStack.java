package thedrake;

import thedrake.model.BoardPos;

public class PlaceFromStack extends Move {

	public PlaceFromStack(BoardPos target) {
		super(target);
	}

	@Override
	public GameState execute(GameState originState) {
		return originState.placeFromStack(target());
	}
	
}
