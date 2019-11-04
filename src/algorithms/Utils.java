package algorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        if(p.getColor()==Colour.BLACK)
            tmp.addAll(neighbors(points, p, Colour.BLUE, edgeThreshold));

        tmp.addAll(neighbors(points, p, Colour.BLACK, edgeThreshold));

        do {
            result.addAll(tmp);

            HashSet<ColoredPoint> voisinsNoirs = (HashSet<ColoredPoint>) tmp
                    .stream()
                    .map(e->neighbors(points, e, Colour.BLACK, edgeThreshold))
                    .flatMap(e->e.stream())
                    .collect(Collectors.toSet());

            HashSet<ColoredPoint> voisinsBleus = (HashSet<ColoredPoint>) tmp
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
        return b;
    }
/*
    protected static BlackBlueComponent blackBlueComponent
            (ArrayList<ColoredPoint> points, ColoredPoint p, int edgeThreshold){
        ArrayList<ColoredPoint> blueNeighbors = neighbors(points, p, Colour.BLUE, edgeThreshold);
        HashSet<ColoredPoint> blackCloud = new HashSet<>(); blackCloud.add(p);
        blackCloud.addAll(cloud(points, p, Colour.BLACK, edgeThreshold));

        System.out.println("BlackCloud : "+blackCloud+"\n\n");
        BlackBlueComponent blackBlueComponent = new BlackBlueComponent();
        blackBlueComponent.blackBlueComponent.addAll(blueNeighbors);
        blackBlueComponent.blackBlueComponent.addAll(blackCloud);

        blackBlueComponent.blackBlueComponent.addAll(blackCloud.stream()
                .map(e->neighbors(points, p, Colour.BLUE, edgeThreshold))
                .flatMap(e->e.stream())
                .collect(Collectors.toList()));

        return blackBlueComponent;
    }*/

    /*protected static boolean equalsBlackBlueComponent
            (ArrayList<ColoredPoint> bbc1, ArrayList<ColoredPoint> bbc2){
        return bbc1.stream().filter(e->e.getColor()==Colour.BLUE).anyMatch(bbc2::contains);
    }*/
}
