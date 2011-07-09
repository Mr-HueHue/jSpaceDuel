package sft.jspaceduel.physics.spaceobjects;

import sft.jspaceduel.physics.PhysicsEngine;

/**
 *
 * @author JJ
 */
public class Star extends CelestialBody implements LightSource {
    public double luminosity;
    
    public Star(PhysicsEngine reg, double[] pos, double[] v, double mass, double angle, double angularVelocity) {
        super(reg, pos, v, mass, angle, angularVelocity, getRadius());
        calcLuminosity();
        reg.registerLightSource(this);
    }
    
    public void setMass(double mass) {
        this.mass = mass;
        calcLuminosity();
    }
    
    public double getMass() {
        return mass;
    }
    
    public double getLuminosity(){
        return luminosity;
    }
    
    public double getFlux(double[] pos){
        double deltaX=pos[0]-posX;
        double deltaY=pos[1]-posY;
        return luminosity/((deltaX*deltaX)+(deltaY*deltaY));
    }
       
    private void calcLuminosity() {
        luminosity = Math.pow(mass*1.98290e-23,3.5);
    }  
    
    public double getRadius() {
        return 1.61978825e-15*Math.pow(mass,0.78);
    }
}
