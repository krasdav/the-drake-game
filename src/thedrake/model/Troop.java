package thedrake.model;

import thedrake.JSONSerializable;
import thedrake.TroopAction;
import thedrake.enums.TroopFace;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Troop implements JSONSerializable {

    private final Offset2D aversPivot;
    private final Offset2D reversePivot;
    private final List<TroopAction> aversAction;
    private final List<TroopAction> reverseAction;
    private final String name;

    // Hlavní konstruktor
    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, final List<TroopAction> aversAction, final List<TroopAction> reverseAction){
        this.aversPivot = aversPivot;
        this.reversePivot = reversPivot;
        this.name = name;
        this.aversAction = new ArrayList<>(aversAction);
        this.reverseAction = new ArrayList<>(reverseAction);
    }

    // Konstruktor, který nastavuje oba pivoty na stejnou hodnotu
    public Troop(String name, Offset2D pivot, final List<TroopAction> aversAction, final List<TroopAction> reverseAction){
        this( name, pivot, pivot, new ArrayList<>(aversAction), new ArrayList<>(reverseAction));
    }

    // Konstruktor, který nastavuje oba pivoty na hodnotu [1, 1]
    public Troop(String name, final List<TroopAction> aversAction, final List<TroopAction> reverseAction){
        this(name, new Offset2D(1,1), new Offset2D(1,1), new ArrayList<>(aversAction), new ArrayList<>(reverseAction));
    }

    public List<TroopAction> actions(TroopFace face){
        if ( face == TroopFace.AVERS){
            return Collections.unmodifiableList(aversAction);
        }
        return Collections.unmodifiableList(reverseAction);
    }

    // Vrací jméno jednotky
    public String name(){
        return name;
    }

    // Vrací pivot na zadané straně jednotky
    public Offset2D pivot(TroopFace face){
        return ( face.equals(TroopFace.AVERS) ? aversPivot : reversePivot ) ;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"",name);
    }

}
