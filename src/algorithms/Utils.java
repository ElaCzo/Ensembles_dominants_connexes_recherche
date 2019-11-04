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

    protected static ColoredPoint greyNode(ArrayList<ColoredPoint> points){
        ArrayList<ColoredPoint> result = (ArrayList<ColoredPoint>) points
                .stream()
                .filter(e -> e.getColor()==Colour.GREY)
                .collect(Collectors.toList());
        if(result.size()==0)
            return null;
        else
            return result.get(0);
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

    private static ArrayList<ColoredPoint> cloud
            (ArrayList<ColoredPoint> points, ColoredPoint p, HashSet<ColoredPoint> result,              int edgeThreshold){
        return (ArrayList<ColoredPoint>) points.stream()
                .filter(e->!result.contains(e)
                        && e.getColor()==Colour.BLACK
                        && p.distance(e)<edgeThreshold)
                .collect(Collectors.toList());
    }

    /* points de la même couleur BLACKBLUE pour lesquels il existe
    un chemin dans cet ensemble qui mène au point en paramètre. */
    /* On ignore les connexions BLUE-BLUE. */
    protected static BlackBlueComponent bBC
    (ArrayList<ColoredPoint> points, ColoredPoint p, int edgeThreshold){
        HashSet<ColoredPoint> result = new HashSet<>();
        HashSet<ColoredPoint> tmp = new HashSet<>();
        HashSet<ColoredPoint> tmp2 = new HashSet<>();

        if(BlackBlueComponent.liste.containsKey(p))
            return BlackBlueComponent.liste.get(p);

        if(p.getColor()==Colour.BLACK)
            tmp.addAll(neighbors(points, p, Colour.BLUE, edgeThreshold));

        tmp.addAll(neighbors(points, p, Colour.BLACK, edgeThreshold));

        do {

            tmp2.addAll(tmp);
            tmp2.removeAll(result);

            result.addAll(tmp);

            HashSet<ColoredPoint> voisinsNoirs = (HashSet<ColoredPoint>) tmp2
                    .stream()
                    .map(e->neighbors(points, e, Colour.BLACK, edgeThreshold))
                    .flatMap(e->e.stream())
                    .collect(Collectors.toSet());

            HashSet<ColoredPoint> voisinsBleus = (HashSet<ColoredPoint>) tmp2
                    .stream()
                    .filter(e->e.getColor()==Colour.BLACK) // que les noirs qui peuvent avoir des voisins bleus
                    .map(e->neighbors(points, e, Colour.BLUE, edgeThreshold))
                    .flatMap(e->e.stream())
                    .collect(Collectors.toSet());

            tmp.addAll(voisinsBleus);
            tmp.addAll(voisinsNoirs);

        }while(!tmp.equals(result));


        BlackBlueComponent b = new BlackBlueComponent();
        b.blackBlueComponent.addAll(result);
        b.blackBlueComponent.stream().forEach(e->
        {if(!BlackBlueComponent.liste.containsKey(e))
            BlackBlueComponent.liste.put(e, b);});

        return b;
    }
}
