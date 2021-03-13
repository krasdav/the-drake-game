package thedrake.enums;

import thedrake.JSONSerializable;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable {
	VICTORY("VICTORY", 0), DRAW("DRAW", 1 ), IN_PLAY("IN_PLAY",2 );

	private GameResult(String name, int numb) {
	}


	@Override
	public void toJSON(PrintWriter writer) {
   		writer.print("\"" + this.toString() + "\"");
	}
}
