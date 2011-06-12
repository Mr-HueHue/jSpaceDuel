/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sft.jspaceduel.util;

/**
 *
 * @author michael
 */
public class AstroPhysics {
    public static double starLuminosity(double mass) {
        return Math.pow(mass*1.98290e-23,3.5);
    }
    
    public static double starRadius(double mass) {
        return 1.61978825e-15*Math.pow(mass,0.78);
    }
    
    public static double starTemperature(double luminosity,double radius) {
        return 7.26952585e-6*Math.pow(luminosity/(radius*radius), 0.25);
    }
}
