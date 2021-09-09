import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class TSPSolver {
    public static ArrayList<City> readFile(String filename) {
        ArrayList<City> cities = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line = null;
            while((line = in.readLine()) != null) {
                String[] blocks = line.trim().split("\\s+");
                if (blocks.length == 3) {
                    City c = new City();
                    c.city = Integer.parseInt(blocks[0]);
                    c.x = Double.parseDouble(blocks[1]);
                    c.y = Double.parseDouble(blocks[2]);
                    //System.out.printf("City %s %f %f\n", c.city, c.x, c.y);
                    cities.add(c);
                } else {
                    continue;
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        City.distances = new double[cities.size()][cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            City ci = cities.get(i);
            for (int j = i; j < cities.size(); j++) {
                City cj = cities.get(j);
                City.distances[i][j] = City.distances[j][i] = Math.sqrt(Math.pow((ci.x - cj.x),2) + Math.pow((ci.y - cj.y),2));
            }
        }
        return cities;
    }

    public static ArrayList<City> solveProblem(ArrayList<City> citiesToVisit) {
        ArrayList<City> routine = new ArrayList<City>();
        City start = null;
        City current = null;
        // get city 0;
        for (int i = 0; i < citiesToVisit.size(); i++) {
            if (citiesToVisit.get(i).city == 0) {
                start = current = citiesToVisit.remove(i);
                routine.add(current);
                break;
            }
        }
        if (current == null) {
            System.out.println("Your problem instance is incorrect! Exiting...");
            System.exit(0);
        }
        // visit cities
        while (!citiesToVisit.isEmpty()) {
            double minDist = Double.MAX_VALUE;
            int index = -1;
            for (int i = 0; i < citiesToVisit.size(); i++) {
                double distI = current.distance(citiesToVisit.get(i));
                // index == -1 is needed in case the distance is really Double.MAX_VALUE.
                if (index == -1 || distI < minDist) {
                    index = i;
                    minDist = distI;
                }
            }
            //int index = 0;

            current = citiesToVisit.remove(index);
            routine.add(current);
        }
        routine.add(start); // go back to 0
        return routine;
    }

    public static double printSolution(ArrayList<City> routine) {
        double totalDistance = 0.0;
        for (int i = 0; i < routine.size(); i++) {
            if (i != routine.size() - 1) {
                System.out.print(routine.get(i).city + "->");
                totalDistance += routine.get(i).distance(routine.get(i+1));
            } else {
                System.out.println(routine.get(i).city);
            }
        }
        return totalDistance;
    }

    /*
        Just evaluate the total distance. A simplified version of printSolution()
     */
    public static double evaluateRoutine(ArrayList<City> routine) {
        double totalDistance = 0.0;
        for (int i = 0; i < routine.size() - 1; i++) {
            totalDistance += routine.get(i).distance(routine.get(i+1));
        }
        return totalDistance;
    }

    /*
        Moves the city at index "from" to index "to" inside the routine
     */
    private static void moveCity(ArrayList<City> routine, int from, int to) {
        // provide your code here.
    }

    /*
        Evaluate the relocation of city and returns the change in total distance.
        The return value is (old total distance - new total distance).
        As a result, a positive value means that the relocation of city results in routine improvement;
        a negative value means that the relocation leads to worse routine. A zero value means same quality.
     */
    public static double evalMove(ArrayList<City> routine, int from, int to) {
        // your implementation goes here
        return 0.0;
    }

    public static boolean moveFirstImprove(ArrayList<City> routine) {
        // your implementation goes here
        return false;
    }

    public static void swapCity(ArrayList<City> routine, int index1, int index2) {
        // your implementation goes here
    }

    /*
        Can you improve the performance of this method?
        You are allowed to change the implementation of this method and add other methods.
        but you are NOT allowed to change its method signature (parameters, name, return type).
     */
    public static double evalSwap(ArrayList<City> routine, int index1, int index2) {
        double oldDistance = evaluateRoutine(routine);
        swapCity(routine, index1, index2);
        double newDistance = evaluateRoutine(routine);
        swapCity(routine, index1, index2);
        return oldDistance - newDistance;
    }

    /*
        This function iterate through all possible swapping positions of cities.
            if a city swap is found to lead to shorter travelling distance, that swap action
            will be applied and the function will return true.
            If there is no good city swap found, it will return false.
     */
    public static boolean swapFirstImprove(ArrayList<City> routine) {
        for (int i = 1; i < routine.size() - 1; i++) {
            for (int j = i + 1; j < routine.size() - 1; j++) {
                double diff = evalSwap(routine, i, j);
                if (diff - 0.00001 > 0) { // I really mean diff > 0 here
                    swapCity(routine, i, j);
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<City> improveRoutine(ArrayList<City> routine) {
        // Can you improve this simple algorithm a bit?
        swapFirstImprove(routine);
        moveFirstImprove(routine);
        return routine;
    }
}
