/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cw2;

/**
 *
 * @author 1825179
 */
import java.util.HashMap;

class City {
    public int city;
    public double x;
    public double y;

    public static double[][] distances;
    
    public double distance(City b) {
        return distances[this.city][b.city];
    }
}
