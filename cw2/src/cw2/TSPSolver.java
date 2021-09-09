package cw2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class TSPSolver {
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

            current = citiesToVisit.remove(index);
            routine.add(current);
        }
        routine.add(start); 
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

    
    public static double evaluateRoutine(ArrayList<City> routine, int from, int to) {
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
        City temp = routine.get(from);
        if (from < to){
            for (int i=from; i<to-1; ++i){
                routine.set(i, routine.get(i+1));
            }
            routine.set(to-1, temp);
        } else{
            for (int i=from; i>to; --i){
                routine.set(i, routine.get(i-1));
            }
            routine.set(to, temp);
        }
    }

    public static double evalMove(ArrayList<City> routine, int from, int to) {
        double oldDis;
        double newDis;
     
       if(from>to || from+1<to){
            oldDis=routine.get(to-1).distance(routine.get(to))+routine.get(from).distance(routine.get(from-1))+routine.get(from).distance(routine.get(from+1));
            newDis=routine.get(to-1).distance(routine.get(from))+routine.get(to).distance(routine.get(from))+routine.get(from-1).distance(routine.get(from+1));
           return oldDis-newDis;
         }
       return 0;
    }

    public static boolean moveFirstImprove(ArrayList<City> routine) {
        // your implementation goes here
        for (int i = 1; i < routine.size() - 1; i++) {
            for (int j = i + 1; j < routine.size() - 1; j++) {
                double diff = evalMove(routine, i, j);
                if (diff - 0.00001 > 0) { 
                    moveCity(routine, i, j);
                    return true;
                }
            }
        }
        return false;
    }


public static void swapCity(ArrayList<City> routine, int index1, int index2) {
        // your implementation goes here
        City first= routine.get(index1);
        City second= routine.get(index2);
        City third=new City();
        third=first;
        first=second;
        second=third;
        
    }

       public static double evalSwap(ArrayList<City> routine, int index1, int index2) {
           
        double Old=routine.get(index1-1).distance(routine.get(index1))+routine.get(index1).distance(routine.get(index1+1))+routine.get(index2-1).distance(routine.get(index2))+routine.get(index2).distance(routine.get(index2+1));
        swapCity(routine, index1, index2);
        double New=routine.get(index1-1).distance(routine.get(index1))+routine.get(index1).distance(routine.get(index1+1))+routine.get(index2-1).distance(routine.get(index2))+routine.get(index2).distance(routine.get(index2+1));
        swapCity(routine, index1, index2);
        return Old-New;
    }
     
    
    public static boolean swapFirstImprove(ArrayList<City> routine) {
        for (int i = 1; i < routine.size() - 1; i++) {
            for (int j = i + 1; j < routine.size() - 1; j++) {
                double diff = evalSwap(routine, i, j);
                if (diff - 0.00001 > 0) { 
                    swapCity(routine, i, j);
                    return true;
                }
            }
        }
        return false;
    }

    
    /* A new method , similar to swap and move */
     public static void inverseCity(ArrayList<City> routine, int index1, int index2){
        int j=index2;
        for(int i=index1; i<=(index1+index2)/2; i++){
            swapCity(routine,i, j);
            j--;
        }   
    }
    public static double evalInverse(ArrayList<City> routine, int index1, int index2){
        double Old=routine.get(index1-1).distance(routine.get(index1))+routine.get(index1).distance(routine.get(index1+1))+routine.get(index2-1).distance(routine.get(index2))+routine.get(index2).distance(routine.get(index2+1));
        inverseCity(routine, index1, index2);
        double New=routine.get(index1-1).distance(routine.get(index1))+routine.get(index1).distance(routine.get(index1+1))+routine.get(index2-1).distance(routine.get(index2))+routine.get(index2).distance(routine.get(index2+1));
        inverseCity(routine, index1, index2);
        return Old-New;

    }

    public static boolean inverseImprove(ArrayList<City> routine){
          for(int i=1; i<routine.size()-1;i++){
            for(int j=i+1;j<routine.size()-1;j++){
                double diff=evalInverse(routine,i,j);
                if(diff>0){
                    inverseCity(routine,i, j);
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<City> improveRoutine(ArrayList<City> routine) {

        double T0 = 4;
        double Tend = 1e-14;
        int loop = 2500;
        double p;
        double coolingRate = 0.995;
        double pSwap = 0;
        double pMove = 0.3;
        double pInverse = 0.6;
        while(true){
            if(T0-Tend<=0)
                break;
                for(int i=0; i<loop; i++){
                    int m = 1+(int)(Math.random()*(routine.size()-3));
                    int n = 1+(int)(Math.random()*(routine.size()-2));
                    int k = chooseMethod(pSwap,pMove,pInverse);
                    double delt; 
                    switch (k) {
                        case 0:
                            delt = evalSwap(routine, m, n);
                            break;
                        case 1:
                            delt = evalMove(routine, m, n);
                            break;
                        default:
                            delt = evalInverse(routine, m, n);
                            break;
                        }  
                   if(delt>=0){
                        operation(k, routine, m, n);
                    } else {
                        p=Math.exp(delt/T0);
                        if(Math.random()<p){
                            operation(k, routine, m, n);
                        }
                    } 
                }
                T0 = T0 * coolingRate;
        }
        return routine;
    }
    /* Choose a method from swap, move and reverse using roulette wheel   */
    public static int chooseMethod(double p1, double p2, double p3){
        double[] probs = new double[3];
        probs[0] = p1;
        probs[1] = p2;
        probs[2] = p3;
        double sum = 0.0;
        double r = Math.random();
        for(int i=0;i<3; i++){
            sum += probs[i];
            if(sum>r){
                return i;
            }
        }
        return probs.length-1;
    }
    public static void operation(int number, ArrayList<City> routine, int index1, int index2){
        switch (number) {
            case 0:
                swapCity(routine, index1, index2);
                break;
            case 1:
                moveCity(routine, index1, index2);
                break;
           default:
               inverseCity(routine, index1, index2);
                break;
        }
    }    
    
}

