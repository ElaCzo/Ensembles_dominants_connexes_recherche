package algorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    protected static ArrayList<Colored_Point> toColoredPoint(ArrayList<Point> p){
        return (ArrayList<Colored_Point>) p.stream().map(Colored_Point::new)
                .collect(Collectors.toList());
    }

    protected static void mark(ArrayList<Colored_Point> points, ArrayList<Point> toMark, Colour c){
        points.stream().filter(e-> toMark.contains(e)).forEach(e->{e.setColor(c);});
    }

    protected static Colored_Point greyNode(ArrayList<Colored_Point> points){
        Stream stream = points.stream().filter(e -> e.getColor()==Colour.BLACK);
        return (Colored_Point) stream.findFirst().get();
    }

    protected static ArrayList<Colored_Point> neighbors
            (ArrayList<Colored_Point> points, Colored_Point p, int edgeThreshold){
        return (ArrayList<Colored_Point>)points.stream()
                .filter(e -> !e.equals(p) && e.distance(p)<edgeThreshold)
                .collect(Collectors.toList());
    }

    protected static ArrayList<Colored_Point> neighbors
            (ArrayList<Colored_Point> points, Colored_Point p, Colour c, int edgeThreshold){
        return (ArrayList<Colored_Point>) neighbors(points, p, edgeThreshold).stream().filter(e->e.getColor()==c).collect(Collectors.toList());
    }

    private static ArrayList<Colored_Point> cloud
            (ArrayList<Colored_Point> points, Colored_Point p, HashSet<Colored_Point> result, Colour c,
             int edgeThreshold){
        return (ArrayList<Colored_Point>) points.stream()
                .filter(e->!result.contains(e) && e.getColor()==c && p.distance(e)<edgeThreshold)
                .collect(Collectors.toList());
    }

    /* points de la même couleur c pour lesquels il existe
    un chemin dans cet ensemble qui mène au point en paramètre. */
    protected static ArrayList<Colored_Point> cloud
    (ArrayList<Colored_Point> points, Point p, Colour c, int edgeThreshold){
        HashSet<Colored_Point> result = new HashSet<>();
        HashSet<Colored_Point> tmp = new HashSet<>();;

        tmp.addAll(neighbors(points, p, Colour.BLUE, edgeThreshold));
        do {
            result.addAll(tmp);
            tmp.addAll(points
                    .stream()
                    .map(e->cloud(points, e, result, Colour.BLUE, edgeThreshold))
                    .flatMap(e->e.stream())
                    .collect(Collectors.toSet()));
        }while(!tmp.equals(result));


        ArrayList<Colored_Point> r = new ArrayList();
        r.addAll(result);
        return r;
    }

    protected static ArrayList<Colored_Point> blackBlueComponent
            (ArrayList<Colored_Point> points, Colored_Point p, int edgeThreshold){
        ArrayList<Colored_Point> blackNeighbors = neighbors(points, p, Colour.BLACK, edgeThreshold);
        ArrayList<Colored_Point> blueCloud = cloud(points, p, Colour.BLUE, edgeThreshold);

        HashSet<Colored_Point> blackBlueComponent = new ArrayList<>();
        blackBlueComponent.addAll(blackNeighbors);
        blackBlueComponent.addAll(blueCloud);

        blackBlueComponent.addAll(blueCloud.stream()
                .map(e->neighbors(points, p, Colour.BLACK, edgeThreshold)).flatMap(e->e.stream())
                .collect(Collectors.toList()));

        ArrayList<Colored_Point> result = new ArrayList<>();
        result.addAll(blackBlueComponent);
        return result;
    }
}
