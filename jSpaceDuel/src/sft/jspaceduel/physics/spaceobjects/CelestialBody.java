package sft.jspaceduel.physics.spaceobjects;

import sft.jspaceduel.physics.AstroPhysics;
import sft.jspaceduel.physics.Kinematics;
import sft.jspaceduel.physics.PhysicsEngine;

/**
 * This abstract class is to be inherited by all celestial (non-artificial) space objects,
 * i.e. Stars or Planets.
 * @author michael
 */
public abstract class CelestialBody extends SpaceObject implements Attractor, Collider {
    /**
     * This variable stores the body's mass.
     */
    public double mass;
    
    /**
     * This method implements the attract() method as required by the Attractor interface.
     * Since the kind of force is of gravitational nature here, we simply need to accelerate
     * the given object.
     * @param object The object that is to be attracted.
     */
    @Override
    public void attract(SpaceObject object){
        //get the positional delta
        double[] delta = getPosDelta(object);
        
        double distsquared = delta[0]*delta[0]+delta[1]*delta[1];
        //since gravity follows the 1/r² law, to get the acceleration per radius we need to divide
        //by r³ or sqrt(r²)*r²
	double accelerationPerRadius = AstroPhysics.G * this.getMass() / (Math.sqrt(distsquared) * distsquared);
        
        object.accelerate(new double[]{delta[0]*accelerationPerRadius, delta[1]*accelerationPerRadius});
    }
    
    /**
     * This is the body's main constructor. It sets the mass and passes all other parameters to the superconstructor.
     * @param engine A referrer to the main physics engine. 
     * @param kin The object's initial kinematic parameters.
     * @param mass The object's initial mass.
     */
    public CelestialBody(PhysicsEngine engine, Kinematics kin, double mass) {
        super(engine, kin);
        this.mass = mass;
    }
       
    /**
     * This method returns the body's mass
     * @return The body's mass, measured in kg.
     */
    @Override
    public double getMass() {
        return mass;
    }
    
    /**
     * This method set's the body's new mass.
     * @param mass The body's new mass, measured in kg.
     */
    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }
}
