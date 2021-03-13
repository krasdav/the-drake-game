package thedrake;

import thedrake.model.BoardPos;
import thedrake.enums.PlayingSide;
import thedrake.model.Offset2D;

import java.util.Collections;
import java.util.List;

public class StrikeAction extends TroopAction {

    public StrikeAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    public StrikeAction(Offset2D offset) {
        super(offset);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        TilePos option = origin.stepByPlayingSide(offset(),side);
        if( state.canCapture(origin,option)){
            return Collections.singletonList(new CaptureOnly(origin,(BoardPos)option));
        }
        return Collections.emptyList();
    }
}
