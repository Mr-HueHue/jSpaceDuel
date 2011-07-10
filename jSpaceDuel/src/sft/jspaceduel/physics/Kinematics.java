package sft.jspaceduel.physics;

import java.io.Serializable;

/**
 * This class is a container for all positional parameters each space object
 * requires in a 2D space. This includes the three degrees of freedom (one
 * rotational and two positional) as well as their first time derivates.
 * Basic units are, as everywhere, meters, degrees and nanoseconds.
 * 
 * @author michael
 */
public class Kinematics implements Serializable {
    /**
     * The position as a 2-element double array. Values are given in meters.
     */
    public double[] x;
    
    /**
     * The object's velocity as a 2-element double array.
     * Values are given in m/s.
     */
    public double[] v;
    
    /** 
     * The acceleration that will be applied at the next tick as a 2-element
     * double array. Values are given in m/s² and reset to 0 after the tick.
     */
    private double[] a;
    
    /**
     * The current rotation angle in degrees.
     */
    public double phi;
    
    /**
     * The current angular velocity in degrees/second.
     */
    public double omega;
    
    /**
     * The angular acceleration that will be applied at the next tick.
     * Values are given in degrees/s²
     */
    private double omegaDot;
       
    /**
     * This constructor will create a new positional parameter set based on the
     * given parameters.
     * 
     * @param x The position as a 2-element double array. Values are given in meters.
     * @param v The first time derivate of the position. Values are given in meters/nanosecond.
     * @param phi The current rotation angle, given in degrees [0-360)
     * @param omega The first time derivate of phi, given in degrees/nanosecond.
     */
    public Kinematics(double[] x, double[] v, double phi, double omega) {
        this.x = x;
        this.phi = phi%360;
        this.v = v;
        this.omega = omega;
        this.a = new double[]{0, 0};
        this.omegaDot = 0;
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
    public Kinematics(double x, double y, double vx, double vy, double phi, double omega) {   
        this(new double[]{x,y},new double[]{vx,vy},phi,omega);
    }
    
    /**
     * This method will change velocities, move and rotate the object based on
     * it's current acceleration values and velocities.
     * 
     * @param deltaT The timespan that has passed since the last tick, measured
     * in seconds.
     */
    public void tick(long deltaT) {
        x[0]  += (v[0]+0.5*a[0]*deltaT)*deltaT;
        x[1]  += (v[1]+0.5*a[1]*deltaT)*deltaT;
        phi   += ((omega+0.5*omegaDot*deltaT)*deltaT)%360;
        v[0]  += a[0]*deltaT;
        v[1]  += a[1]*deltaT;
        omega += omegaDot*deltaT;
    }
    
    /**
     * This method will add the given acceleration to the next tick's total
     * acceleration.
     * 
     * @param a The acceleration, measured in m/s²
     */
    public void accelerate(double[] a) {
        this.a[0] += a[0];
        this.a[1] += a[1];
    }
    
    /*
     * This method will add the given angular acceleration to the next tick's
     * total acceleration.
     * 
     * @param omegadot The angular acceleration, measured in degrees/s²
     */
    public void accelerateAngular(double omegaDot) {
        this.omegaDot += omegaDot;
    }
}
