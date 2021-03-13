package thedrake.model;

import thedrake.*;
import thedrake.Move;
import thedrake.TroopAction;
import thedrake.enums.PlayingSide;
import thedrake.enums.TroopFace;
import thedrake.GameState;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class TroopTile implements Tile, JSONSerializable {

    private Troop troop;
    private PlayingSide side;
    private TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side(){ return this.side; }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face(){ return this.face; }

    // Jednotka, která stojí na této dlaždici
    public Troop troop(){ return this.troop; }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    public boolean canStepOn(){
        return false;
    }

    // Vrací True
    public boolean hasTroop(){
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
       List<Move> result = new ArrayList<>();
       for( TroopAction action : troop.actions(face)){
           result.addAll(action.movesFrom(pos,side,state));
       }
       return result;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)
    public TroopTile flipped(){
        if( face == TroopFace.AVERS){
            return new TroopTile( troop, side, TroopFace.REVERS);
        }else {
            return new TroopTile( troop, side, TroopFace.AVERS);
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"troop\":");
        troop.toJSON(writer);
        writer.printf(",\"side\":");
        side.toJSON(writer);
        writer.printf(",\"face\":");
        face.toJSON(writer);
        writer.printf("}");
    }
}
