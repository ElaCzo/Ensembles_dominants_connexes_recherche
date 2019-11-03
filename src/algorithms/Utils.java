package algorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    protected static ArrayList<Colored_Point> toColoredPoint(ArrayList<Point> p){
        return (ArrayList<Colored_Point>) p.stream().map(Colored_Point::new).collect(Collectors.toList());
    }

    protected static void mark(ArrayList<Colored_Point> points, ArrayList<Point> toMark, Colour c){
        points.stream().filter(e-> toMark.contains(e)).forEach(e->{e.setColor(c);});
    }

    protected static Colored_Point greyNode(ArrayList<Colored_Point> points){
        Stream stream = points.stream().filter(e -> e.getColor()==Colour.BLACK);
        return (Colored_Point) stream.findFirst().get();
    }

    protected static ArrayList<Colored_Point> neighboors(ArrayList<Colored_Point> points, Colored_Point p, int edgeThreshold){
        return (ArrayList<Colored_Point>)points.stream().filter(e -> e.distance(p)<edgeThreshold).collect(Collectors.toList());
    }
}
