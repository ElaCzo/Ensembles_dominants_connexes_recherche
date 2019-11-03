package algorithms;

import java.awt.*;

public class Colored_Point extends Point {
    private Colour color = Colour.WHITE;

    public Colored_Point(Point p){
        super(p);
    }

    public Colour getColor() {
        return color;
    }

    public void setColor(Colour c) {
        color = c;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Point)
        return super.equals((Point)o);
        return false;
    }
}