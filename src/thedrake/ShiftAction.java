package thedrake;

import thedrake.model.BoardPos;
import thedrake.enums.PlayingSide;
import thedrake.model.Offset2D;

import java.util.ArrayList;
import java.util.List;

public class ShiftAction extends TroopAction {

	public ShiftAction(Offset2D offset) {
		super(offset);
	}

	public ShiftAction(int offsetX, int offsetY) {
		super(offsetX, offsetY);
	}

	@Override
	public List<Move> movesFrom(final BoardPos origin, final PlayingSide side, final GameState state) {
		final List<Move> result = new ArrayList<>();
		final TilePos target = origin.stepByPlayingSide(this.offset(), side);
		if (state.canStep(origin, target)) {
			result.add(new StepOnly(origin, (BoardPos)target));
		}
		else if (state.canCapture(origin, target)) {
			result.add(new StepAndCapture(origin, (BoardPos)target));
		}
		return result;
	}
}
