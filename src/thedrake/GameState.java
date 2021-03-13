package thedrake;

import thedrake.model.*;
import thedrake.enums.GameResult;
import thedrake.enums.PlayingSide;

import java.io.PrintWriter;
import java.util.Optional;

public class GameState implements JSONSerializable {
	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy) {
		this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy, 
			PlayingSide sideOnTurn, 
			GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
	}
	
	public Board board() {
		return board;
	}
	
	public PlayingSide sideOnTurn() {
		return sideOnTurn;
	}
	
	public GameResult result() {
		return result;
	}
	
	public Army army(PlayingSide side) {
		if(side == PlayingSide.BLUE) {
			return blueArmy;
		}
		
		return orangeArmy;
	}
	
	public Army armyOnTurn() {
		return army(sideOnTurn);
	}
	
	public Army armyNotOnTurn() {
		if(sideOnTurn == PlayingSide.BLUE)
			return orangeArmy;
		
		return blueArmy;
	}
	
	public Tile tileAt(TilePos pos) {
		Optional<TroopTile>  troop = blueArmy.boardTroops().at(pos);
		if( troop.isPresent()){
			return troop.get();
		}
		troop = orangeArmy.boardTroops().at(pos);
		if ( troop.isPresent()){
			return troop.get();
		}
		return board.at(pos);
	}

	private boolean canStepFrom(TilePos origin) {
		if ( result != GameResult.IN_PLAY){
			return false;
		}
		if( origin == TilePos.OFF_BOARD){
			return false;
		}
		if( armyOnTurn().boardTroops().guards() < 2 ){
			return false;
		}
		Tile tile = tileAt(origin);
		return tile.hasTroop() && ((TroopTile)tile).side() == sideOnTurn;
	}

	private boolean canStepTo(TilePos target) {
		if( GameResult.IN_PLAY != result){
			return false;
		}
		if ( target == TilePos.OFF_BOARD){
			return false;
		}
		if ( !tileAt(target).canStepOn()){
			return false;
		}
		return true;
	}
	
	private boolean canCaptureOn(TilePos target) {
		if( GameResult.IN_PLAY != result){
			return false;
		}
		if ( target == TilePos.OFF_BOARD){
			return false;
		}
		if ( armyNotOnTurn().boardTroops().at(target).isEmpty()){
			return false;
		}
		Tile tile = tileAt(target);
		return tile.hasTroop() && ((TroopTile)tile).side() != this.sideOnTurn;
	}
	
	public boolean canStep(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canStepTo(target);
	}
	
	public boolean canCapture(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canCaptureOn(target);
	}
	
	public boolean canPlaceFromStack(TilePos target) {
		if( GameResult.IN_PLAY != result){
			return false;
		}
		if ( target == TilePos.OFF_BOARD){
			return false;
		}
		if ( !tileAt(target).canStepOn()){
			return false;
		}

		if( armyOnTurn().stack().isEmpty() ){
			return false;
		}

		Army onTurn = this.armyOnTurn();

		if (!onTurn.boardTroops().isLeaderPlaced()) {
			if (this.sideOnTurn == PlayingSide.BLUE) {
				return target.j() == 0;
			}
			return target.j() == this.board.dimension() - 1;
		}
		else {
			if (onTurn.boardTroops().isPlacingGuards()) {
				return onTurn.boardTroops().leaderPosition().isNextTo(target);
			}
			for (final BoardPos pos : onTurn.boardTroops().troopPositions()) {
				if (pos.isNextTo(target)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public GameState stepOnly(BoardPos origin, BoardPos target) {		
		if(canStep(origin, target))		 
			return createNewGameState(
					armyNotOnTurn(),
					armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
		
		throw new IllegalArgumentException();
	}
	
	public GameState stepAndCapture(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target), 
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState captureOnly(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState placeFromStack(BoardPos target) {
		if(canPlaceFromStack(target)) {
			return createNewGameState(
					armyNotOnTurn(), 
					armyOnTurn().placeFromStack(target), 
					GameResult.IN_PLAY);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState resign() {
		return createNewGameState(
				armyNotOnTurn(), 
				armyOnTurn(), 
				GameResult.VICTORY);
	}
	
	public GameState draw() {
		return createNewGameState(
				armyOnTurn(), 
				armyNotOnTurn(), 
				GameResult.DRAW);
	}
	
	private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
		if(armyOnTurn.side() == PlayingSide.BLUE) {
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		}
		
		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result); 
	}

	@Override
	public String toString() {
		return "GameState{" +
				"board=" + board +
				", sideOnTurn=" + sideOnTurn +
				", blueArmy=" + blueArmy +
				", orangeArmy=" + orangeArmy +
				", result=" + result +
				'}';
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"result\":");
		result.toJSON(writer);
		writer.printf(",\"board\":");
		board.toJSON(writer);
		writer.printf(",\"blueArmy\":");
		blueArmy.toJSON(writer);
		writer.printf(",\"orangeArmy\":");
		orangeArmy.toJSON(writer);
		writer.printf("}");
	}
}
