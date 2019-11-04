package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class BlackBlueComponent {
    public HashSet<ColoredPoint> blackBlueComponent;

    public static HashMap<ColoredPoint, BlackBlueComponent> liste = new HashMap<>();

    public BlackBlueComponent(){
        blackBlueComponent= new HashSet<>();
    }

    /* points de la même couleur BLACKBLUE pour lesquels il existe
    un chemin dans cet ensemble qui mène au point en paramètre. */
    /* On ignore les connexions BLUE-BLUE. */
    public static BlackBlueComponent BBC
    (ArrayList<ColoredPoint> points, ColoredPoint p, int edgeThreshold){
        HashSet<ColoredPoint> result1 = new HashSet<>();
        HashSet<ColoredPoint> result2 = new HashSet<>();
        HashSet<ColoredPoint> ajouts = new HashSet<>();

        if(BlackBlueComponent.liste.containsKey(p))
            return BlackBlueComponent.liste.get(p);

        if(p.getColor()==Colour.BLACK)
            result2.addAll(Utils.neighbors(points, p, Colour.BLUE, edgeThreshold));

        result2.addAll(Utils.neighbors(points, p, Colour.BLACK, edgeThreshold));

        do {
            ajouts.addAll(result2);
            ajouts.removeAll(result1);

            result1.addAll(result2);

            HashSet<ColoredPoint> voisinsNoirs = (HashSet<ColoredPoint>) ajouts
                    .stream()
                    .map(e->Utils.neighbors(points, e, Colour.BLACK, edgeThreshold))
                    .flatMap(e->e.stream())
                    .collect(Collectors.toSet());

            HashSet<ColoredPoint> voisinsBleus = (HashSet<ColoredPoint>) ajouts
                    .stream()
                    .filter(e->e.getColor()==Colour.BLACK) // que les noirs qui peuvent avoir des voisins bleus
                    .map(e->Utils.neighbors(points, e, Colour.BLUE, edgeThreshold))
                    .flatMap(e->e.stream())
                    .collect(Collectors.toSet());

            result2.addAll(voisinsBleus);
            result2.addAll(voisinsNoirs);

        }while(!result2.equals(result1));


        BlackBlueComponent b = new BlackBlueComponent();
        b.blackBlueComponent.addAll(result1);
        b.blackBlueComponent.stream().forEach(e->
        {if(!BlackBlueComponent.liste.containsKey(e))
            BlackBlueComponent.liste.put(e, b);});

        return b;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof BlackBlueComponent
                && this.blackBlueComponent.equals(((BlackBlueComponent) o).blackBlueComponent);
    }
}
