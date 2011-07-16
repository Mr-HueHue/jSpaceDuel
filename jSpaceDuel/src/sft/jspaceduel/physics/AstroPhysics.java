package sft.jspaceduel.physics;

/**
 * This class provides some static methods and constants required for astrophysical
 * calculations.
 * @author michael
 */
public class AstroPhysics {
    /**
     * This constant is the value of Newton's constant of gravity, measured
     * in m³/(kg*s²)
     */
    final public static double G = 6.67300e-11;
    
    /**
     * This method returns the luminosity of a given-mass zero-age main sequence star.
     * @param mass The star's mass, measured in kg.
     * @return The star's luminosity, measured in watts.
     */
    public static double zamsStarLuminosity(double mass) {
        return Math.pow(mass*1.98290e-23,3.5);
    }
    
    /**
     * This method returns the radius of a given-mass zero-age main sequence star.
     * @param mass The star's mass, measured in kg.
     * @return  The star's radius, measured in meters.
     */
    public static double zamsStarRadius(double mass) {
        return 1.61978825e-15*Math.pow(mass,0.78);
    }
    
    /**
     * This method returns the temperature of a ideal black body, given luminosity and radius.
     * @param luminosity The object's luminosity, measured in watts.
     * @param radius The object's radius, measured in meters.
     * @return The object's temperature, measured in kelvins.
     */
    public static double blackBodyTemperature(double luminosity,double radius) {
        return 34.42*Math.pow(luminosity/(radius*radius), 0.25);
    }
    
    /**
     * This method returns the temperature of a given-mass zero-age main sequence star.
     * @param mass The star's mass, measured in kg.
     * @return The star's temperature, measured in Kelvins.
     */
    public static double zamsStarTemperature(double mass) {
        return blackBodyTemperature(zamsStarLuminosity(mass),zamsStarRadius(mass));
    }
}
