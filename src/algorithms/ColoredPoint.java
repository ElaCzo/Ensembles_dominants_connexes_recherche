package algorithms;

import java.awt.*;

public class ColoredPoint extends Point {
    private Colour color;

    public ColoredPoint(Point p){
        super(p);
        color = Colour.NOCOLOUR;
    }

    public Colour getColor() {
        return color;
    }

    public void setColor(Colour c) {
        color = c;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ColoredPoint || o instanceof Point)
        return ((Point)o).x==this.x && ((Point)o).y==this.y;
        return false;
    }

    @Override
    public String toString(){
        return super.toString()+" "+color.name();
    }
}