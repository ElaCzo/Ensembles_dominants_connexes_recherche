package algorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Utils {
    protected static ArrayList<ColoredPoint> toColoredPoint(ArrayList<Point> p){
        return (ArrayList<ColoredPoint>) p.stream().map(ColoredPoint::new)
                .collect(Collectors.toList());
    }

    protected static void mark(ArrayList<ColoredPoint> points, ArrayList<Point> toMark, Colour c){
        points.stream().filter(e-> toMark.contains(e)).forEach(e->{e.setColor(c);});
    }

    protected static ArrayList<ColoredPoint> greyNodes(ArrayList<ColoredPoint> points){
        ArrayList<ColoredPoint> result = (ArrayList<ColoredPoint>) points
                .stream()
                .filter(e -> e.getColor()==Colour.GREY)
                .collect(Collectors.toList());
        return result;
    }

    protected static ArrayList<ColoredPoint> neighbors
            (ArrayList<ColoredPoint> points, ColoredPoint p, int edgeThreshold){
        return (ArrayList<ColoredPoint>)points.stream()
                .filter(e -> !e.equals(p) && e.distance(p)<edgeThreshold)
                .collect(Collectors.toList());
    }

    protected static ArrayList<ColoredPoint> neighbors
            (ArrayList<ColoredPoint> points, ColoredPoint p, Colour c, int edgeThreshold){
        return (ArrayList<ColoredPoint>) neighbors(points, p, edgeThreshold).stream().filter(e->e.getColor()==c).collect(Collectors.toList());
    }
}
