import java.util.ArrayList;
import java.util.LinkedList;

/*
    You can modify this file. but I will remove any modification of yours and replace the whole file with mine.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<City> cities = TSPSolver.readFile(args[0]);
        cities = TSPSolver.solveProblem(cities);
        System.out.printf("Distances: %f\n", TSPSolver.printSolution(cities));
        cities = TSPSolver.improveRoutine(cities);
        System.out.printf("After improving: %f\n", TSPSolver.printSolution(cities));
    }
}
