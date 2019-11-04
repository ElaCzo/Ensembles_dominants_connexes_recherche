package algorithms;

import java.awt.*;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class DefaultTeam {

    // Algorithme MIS qui garantit 2 hops :
  public static ArrayList<Point> MIS2
          (ArrayList<Point> points, int edgeThreshold) {
    ArrayList<ColoredPoint> uncoloredNodes = Utils.toColoredPoint(points);
    ArrayList<ColoredPoint> MIS = new ArrayList<>();
    ArrayList<ColoredPoint> candidat = new ArrayList<>();

    Collections.shuffle(uncoloredNodes);
    candidat.add(uncoloredNodes.get(0));

    while(!candidat.isEmpty()){
      ColoredPoint random =candidat.remove(0);
      if(random.getColor()==Colour.GREY)
        continue ;

      random.setColor(Colour.BLACK);
      MIS.add(random);

      for(ColoredPoint pt: uncoloredNodes){ // set all random neighour in gray
        if (pt.distance(random)< edgeThreshold)
          pt.setColor(Colour.GREY);
      }

      for(ColoredPoint pt : uncoloredNodes){
        if (pt.distance(random)< edgeThreshold){
          for(ColoredPoint ptv : uncoloredNodes){
            if(ptv.distance(pt)< edgeThreshold && ptv.getColor()==Colour.NOCOLOUR && !candidat.contains(ptv)){
              candidat.add(ptv);
            }
          }
        }
      }
    }

    MIS.stream().forEach(e->e.setColor(Colour.BLACK));

    // On ajoute les nodes isolées
    MIS.addAll(uncoloredNodes
            .stream()
            .filter(e->Utils.neighbors(uncoloredNodes, e, edgeThreshold).size()==1)
            .collect(Collectors.toList()));

    return (ArrayList<Point>) MIS.stream().map(e->(Point)e).collect(Collectors.toList());
  }

  // sequential algorithm
  private ArrayList<Point> MIS(ArrayList<Point> points, int edgeThreshold){
    ArrayList<Point> I = new ArrayList<>();
    ArrayList<Point> V = (ArrayList)points.clone();

    while(!V.isEmpty()){
      Point v = V.remove(V.size()-1);
      I.add(v);
      for(int i=V.size()-1; i>=0; i--){
        Point n = V.get(i);
        if(n.distance(v)<edgeThreshold)
          V.remove(n);
      }
    }
    return I;
  }

  public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
    ArrayList<Point> mis = MIS2(points, edgeThreshold);

    ArrayList<ColoredPoint> colored_points = Utils.toColoredPoint(points);
    Utils.mark(colored_points, (ArrayList)colored_points, Colour.GREY);
    Utils.mark(colored_points, mis, Colour.BLACK);

    ColoredPoint grey;
    ArrayList<ColoredPoint> greyNodes=Utils.greyNodes(colored_points);

    for(int i=5; i>1; i--){
      int cpt=0;
      while(!greyNodes.isEmpty() && cpt<greyNodes.size()){
        //System.out.println(i);

        grey=greyNodes.get(cpt);

        HashSet<BlackBlueComponent> blackBlueComponents = new HashSet<>();

        // liste chaque voisin noir :
        ArrayList<ColoredPoint> voisins = Utils.neighbors(colored_points, grey, Colour.BLACK, edgeThreshold);

        // blackBlueComponent de chaque voisin
        blackBlueComponents = (HashSet<BlackBlueComponent>) voisins
                .stream()
                .map(e->Utils.bBC(colored_points, e, edgeThreshold))
                .collect(Collectors.toSet());

        cpt++;
        if(blackBlueComponents.size()>=i) {
            System.out.println(blackBlueComponents.size());
            grey.setColor(Colour.BLUE);

            BlackBlueComponent bres = new BlackBlueComponent();

            blackBlueComponents.stream().forEach(e->bres.blackBlueComponent.addAll(e.blackBlueComponent));
            bres.blackBlueComponent.add(grey);

            // on met à jour la liste :
            BlackBlueComponent
                    .liste
                    .keySet()
                    .stream()
                    .filter(e->bres.blackBlueComponent
                    .contains(e))
                    .forEach(e->BlackBlueComponent.liste.put(e, bres));

            greyNodes.remove(grey);
            cpt=0;
        }
      }
    }

    ArrayList<Point> result = new ArrayList<>();
    result.addAll(colored_points
            .stream()
            .filter(e->e.getColor()==Colour.BLUE || e.getColor()==Colour.BLACK) // ajout black
            .collect(Collectors.toList()));

    System.out.println(result);
    return result;
  }
  
  //FILE PRINTER
  private void saveToFile(String filename,ArrayList<Point> result){
    int index=0;
    try {
      while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+Integer.toString(index)+".points")));
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
        }
        index++;
      }
    } catch (FileNotFoundException e) {
      printToFile(filename+Integer.toString(index)+".points",result);
    }
  }
  private void printToFile(String filename,ArrayList<Point> points){
    try {
      PrintStream output = new PrintStream(new FileOutputStream(filename));
      int x,y;
      for (Point p:points) output.println(Integer.toString((int)p.getX())+" "+Integer.toString((int)p.getY()));
      output.close();
    } catch (FileNotFoundException e) {
      System.err.println("I/O exception: unable to create "+filename);
    }
  }

  //FILE LOADER
  private ArrayList<Point> readFromFile(String filename) {
    String line;
    String[] coordinates;
    ArrayList<Point> points=new ArrayList<Point>();
    try {
      BufferedReader input = new BufferedReader(
          new InputStreamReader(new FileInputStream(filename))
          );
      try {
        while ((line=input.readLine())!=null) {
          coordinates=line.split("\\s+");
          points.add(new Point(Integer.parseInt(coordinates[0]),
                Integer.parseInt(coordinates[1])));
        }
      } catch (IOException e) {
        System.err.println("Exception: interrupted I/O.");
      } finally {
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("Input file not found.");
    }
    return points;
  }
}
