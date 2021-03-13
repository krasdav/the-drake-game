package thedrake;

import thedrake.model.BoardPos;
import thedrake.enums.PlayingSide;
import thedrake.model.Offset2D;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    public SlideAction(Offset2D offset) {
        super(offset);
    }

    @Override
    public List<Move> movesFrom(final BoardPos origin, final PlayingSide side, final GameState state) {
        final List<Move> result = new ArrayList<Move>();
        TilePos target;
        for (target = origin.stepByPlayingSide(this.offset(), side); state.canStep(origin, target); target = target.stepByPlayingSide(this.offset(), side)) {
            result.add(new StepOnly(origin, (BoardPos)target));
        }
        if (state.canCapture(origin, target)) {
            result.add(new StepAndCapture(origin, (BoardPos)target));
        }
        return result;
    }
}
