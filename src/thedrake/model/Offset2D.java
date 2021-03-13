package thedrake.model;

public class Offset2D {

    public final int x,y;

    public Offset2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    // Zjištuje, zda se tento offset rovná jinému offsetu
    public boolean equalsTo(int x, int y){
        return x == this.x && y == this.y;
    }

    // Vrací nový offset, kde y souřadnice má obrácené znaménko
    public Offset2D yFlipped(){
        return new Offset2D(x,-y);
    }
}
