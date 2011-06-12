package sft.jspaceduel.spaceObjects;

/**
 * This class is a container for all positional parameters each space object
 * requires in a 2D space. This includes the three degrees of freedom (one
 * rotational and two positional) as well as their first time derivates.
 * Basic units are, as everywhere, meters, degrees and nanoseconds.
 * 
 * @author michael
 */
public class PosParams {
    /**
     * The position as a 2-element double array. Values are given in meters.
     */
    public double[] x;
    private double[] v;
    
    /**
     * The current rotation angle in degrees.
     */
    public double phi;
    private double omega;
       
    /**
     * This constructor will create a new positional parameter set based on the
     * given parameters.
     * 
     * @param x The position as a 2-element double array. Values are given in meters.
     * @param v The first time derivate of the position. Values are given in meters/nanosecond.
     * @param phi The current rotation angle, given in degrees [0-360)
     * @param omega The first time derivate of phi, given in degrees/nanosecond.
     */
    public PosParams(double[] x, double[] v, double phi, double omega) {
        this.x = x;
        this.v = v;
        this.omega = omega;
        this.phi = phi;
    }

    /**
     * This constructor will create a new positional parameter set based on the
     * given parameters.
     * 
     * @param x The x part of the position, given in meters.
     * @param y The y part of the position, given in meters.
     * @param vx The first time derivate of x, given in meters/nanosecond.
     * @param vy The first time derivate of y, given in meters/nanosecond.
     * @param phi The current rotation angle, given in degrees [0-360)
     * @param omega The first time derivate of phi, given in degrees/nanosecond.
     */
    public PosParams(double x, double y, double vx, double vy, double phi, double omega) {       
        this.x = new double[]{x, y};
        this.v = new double[]{vx, vy};
        this.omega = omega;
        this.phi = phi%360;
    }
    
    /**
     * This method will move and rotate the object based on it's current
     * velocities for the given timespan.
     * 
     * @param deltaT The timespan, measured in nanoseconds.
     */
    public void move(long deltaT) {
        this.x[0] += this.v[0]*deltaT;
        this.x[1] += this.v[1]*deltaT;
        this.phi += (this.omega*deltaT)%360;
    }
    
    /**
     * This method will apply the given acceleration for the given timespan.
     * 
     * @param deltaT The timespan, measured in nanoseconds.
     * @param a The acceleration, measured in m/ns²
     */
    public void accelerate(long deltaT, double[] a) {
        this.v[0] += a[0]*deltaT;
        this.v[1] += a[1]*deltaT;
    }
}
