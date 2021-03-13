package thedrake.model;

import thedrake.*;
import thedrake.enums.PlayingSide;
import thedrake.enums.TroopFace;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;
	
	public BoardTroops(PlayingSide playingSide) {
		troopMap = Collections.emptyMap();
		leaderPosition = TilePos.OFF_BOARD;
		guards = 0;
		this.playingSide = playingSide;
	}

	public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		return Optional.ofNullable(troopMap.get(pos));
	}
	
	public PlayingSide playingSide() {
		return playingSide;
	}
	
	public TilePos leaderPosition() {
		return leaderPosition;
	}

	public int guards() {
		return guards;
	}
	
	public boolean isLeaderPlaced() {
		if( leaderPosition==TilePos.OFF_BOARD){
			return false;
		}
		return true;
	}
	
	public boolean isPlacingGuards() {
		if ( leaderPosition != TilePos.OFF_BOARD && guards < 2){
			return true;
		}
		return false;
	}	
	
	public Set<BoardPos> troopPositions() {
		return troopMap.keySet();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		if( at(target).isPresent()){
			throw new IllegalStateException("Position is occupied by another Troop");
		}

		Map<BoardPos, TroopTile> troops = new HashMap<>(troopMap);
		troops.put(target, new TroopTile(troop,playingSide, TroopFace.AVERS));
		int newGuards = guards;
		if( isPlacingGuards() ){
			newGuards++;
		}
		TilePos newLeader = leaderPosition;
		if( newLeader == TilePos.OFF_BOARD ){
			newLeader = target;
		}
		return new BoardTroops(playingSide,troops,newLeader,newGuards);
	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if( !isLeaderPlaced()){
			throw new IllegalStateException("Leader must be placed first");
		}
		if( isPlacingGuards() ){
			throw new IllegalStateException("Guards must be placed first");
		}
		if( at(target).isPresent()){
			throw new IllegalArgumentException("Position is occupied by another Troop");
		}
		if ( !at(origin).isPresent()){
			throw new IllegalArgumentException("Original posin invalid");
		}
		Map<BoardPos,TroopTile> troops = new HashMap<>(troopMap);
		TroopTile tile = troops.remove(origin);
		troops.put(target, tile.flipped());
		TilePos leader = leaderPosition;
		if ( leaderPosition.equals(origin)){
			leader = target;
		}
		return new BoardTroops(playingSide,troops,leader,guards);
	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");			
		}
		
		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");			
		}
		
		if(!at(origin).isPresent())
			throw new IllegalArgumentException();
		
		Map<BoardPos, TroopTile> troops = new HashMap<>(troopMap);
		TroopTile tile = troops.remove(origin);
		troops.put(origin, tile.flipped());

		return new BoardTroops(playingSide, troops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
		if( isPlacingGuards() ){
			throw new IllegalStateException("Guards must be placed first");
		}
		if( !isLeaderPlaced()){
			throw new IllegalStateException("Leader must be placed first");
		}
		if( at(target).isEmpty()){
			throw new IllegalArgumentException("No Troop on the position");
		}
		Map<BoardPos,TroopTile> troops = new HashMap<>(troopMap);
		troops.remove(target);
		TilePos leader = leaderPosition;
		if( leaderPosition.equals(target)){
			leader = TilePos.OFF_BOARD;
		}
		return new BoardTroops( playingSide,troops, leader, guards);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"side\":");
		playingSide.toJSON(writer);
		writer.printf(",\"leaderPosition\":");
		leaderPosition.toJSON(writer);
		writer.printf(",\"guards\":");
		writer.printf("%s",guards);
		writer.printf(",\"troopMap\":{");
		troopMapToJSON(writer);
		writer.printf("}");
	}

	private void troopMapToJSON(PrintWriter writer){
		ArrayList<BoardPos> list = new ArrayList<>(troopMap.keySet());
		Collections.sort(list);
		int cnt=0;
		for( BoardPos pos : list){
			cnt ++;
			pos.toJSON(writer);
			writer.printf(":");
			troopMap.get(pos).toJSON(writer);
			if ( ! (cnt==list.size())){
				writer.printf(",");
			}
		}
		writer.printf("}");
	}

}
