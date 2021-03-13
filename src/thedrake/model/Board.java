package thedrake.model;

import thedrake.*;

import java.io.PrintWriter;

public class Board implements JSONSerializable {
	private final int dimension;
	private final BoardTile[][] tiles;

	// Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
	public Board(final int dimension) {
		this.dimension = dimension;
		tiles = initEmptyTiles(dimension);
	}

	public Board( final int dimension, BoardTile[][] tiles){
		this.dimension = dimension;
		this.tiles = tiles;
	}

	// Rozměr hrací desky
	public int dimension() {
		return dimension;
	}

	// Vrací dlaždici na zvolené pozici.
	public BoardTile at(TilePos pos) {
		return tiles[pos.i()][pos.j()];
	}

	// Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
	public Board withTiles(TileAt... ats) {
		final BoardTile[][] newTiles = new BoardTile[tiles.length][];
		for( int i = 0 ; i < tiles.length ; i++ ){
			newTiles[i] = tiles[i].clone();
		}

		for ( TileAt at : ats){
			newTiles[at.pos.i()][at.pos.j()] = at.tile;
		}
		return new Board( dimension, newTiles);
	}

	// Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
	public PositionFactory positionFactory() {
		return new PositionFactory(dimension);
	}

	private BoardTile[][] initEmptyTiles(int dimension){
		final BoardTile[][] tiles = new BoardTile[dimension][dimension];
		for( int i = 0; i < dimension; i++){
			for( int j = 0; j < dimension; j++){
				tiles[i][j] = BoardTile.EMPTY;
			}
		}
		return tiles;
	}
	
	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;
		
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"dimension\":%d,\"tiles\":[", this.dimension);
		int cnt = 0;
		for ( int i = 0; i < tiles.length ; i++){
			for ( int j = 0 ; j < tiles[i].length;j++){
				cnt++;
				tiles[j][i].toJSON(writer);
				if (cnt < dimension * dimension){
					writer.printf(",");
				}
			}
		}
		writer.printf("]}");
	}
}

