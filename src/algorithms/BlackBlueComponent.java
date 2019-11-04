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

    @Override
    public boolean equals(Object o){
        return o instanceof BlackBlueComponent
                && this.blackBlueComponent.equals(((BlackBlueComponent) o).blackBlueComponent);
    }

    public HashSet<ColoredPoint> blackNodes(){
        return (HashSet<ColoredPoint>) blackBlueComponent
                .stream()
                .filter(e->e.getColor()==Colour.BLACK)
                .collect(Collectors.toSet());
    }
}
