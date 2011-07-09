package sft.jspaceduel.physics.spaceobjects;

import sft.jspaceduel.physics.Kinematics;
import sft.jspaceduel.physics.PhysicsEngine;

/**
 * This class is suitable for all rocky, small planets.
 * It is not suitable for gas giants, since their matter is compressible and therefore
 * radius calculations at collisions are incorrect.
 * @author michael
 */
public class Planet extends CelestialBody {
    /**
     * This variable stores an ID of the planet's texture, for usage by the graphics engine.
     */
    int textureID;
    
    /**
     * This variable stores the planet's radius.
     */
    public double radius;
    
    /**
     * This is the planet's main constructor. It sets the radius and texture ID and passes
     * all other parameters to the superconstructor.
     * @param engine A referrer to the main physics engine.
     * @param kin The planet's initial kinematic parameters.
     * @param mass The planet's initial mass.
     * @param radius The planet's initial radius.
     * @param textureID The planet's initial texture.
     */
    public Planet(PhysicsEngine engine, Kinematics kin, double mass, double radius, int textureID) {
        super(engine, kin, mass);
        this.textureID = textureID;
        this.radius = radius;
    }

    /**
     * This method returns the planet's radius as required by the abstract SpaceObject class.
     * @return The Planet's radius, measured in meters.
     */
    @Override
    public double getRadius() {
        return radius;
    }
    
    /**
     * This method implements the collide() method as required by the Collider interface.
     * It checks if the planet collides with the given object and if it takes the
     * master role in the collision.
     * A planet takes the master role in pretty much every collision, except when colliding
     * with a star or an even bigger planet.
     * The planet then absorbs the object it's colliding with, destroying it and increasing
     * it's own mass and radius.
     * @param object The object that is to be collided with.
     * @return true if an collision has occured.
     */
    @Override
    public boolean collide(SpaceObject object) {
        //first, we need to check if the object is in range.
        double[] delta = getPosDelta(object);        
        double distsquared = delta[0]*delta[0]+delta[1]*delta[1];
        
        if(distsquared > getRadius()*getRadius() + object.getRadius()*object.getRadius())
            return false;
        
        //then, we need to check the type of the other object to determine if we take the
        //master role in the collision. if so, we absorb the object.
        if(object instanceof Planet) {
            if(this.getMass() > object.getMass()) {
                //we absorb a whole planet, which means our surface sureley gets devasted a lot.
               
                //assuming 10 is a texture ID for a devasted planet.
                textureID = 10;
                
                //of course, our radius increases as well. since the planet's materials can be
                //assumed to be incompressible for non-gas giants, we just need to add the volumes.
                radius = Math.pow(radius*radius*radius + object.getRadius()*object.getRadius()*object.getRadius(),1/3);
                
                //finally absorb the planet.
                absorb(object);
                return true;
            }
        }
        if(object instanceof SpaceCraft) {
            absorb(object);
            return true;
        }
        
        return false;
    }
    
    /**
     * This method is called when setting the planet's mass.
     * If the planet's mass would lie above a certain limit, the planet is replaced
     * by a gas giant.
     * @param mass The planet's new mass.
     */
    @Override
    public void setMass(double mass) {
     //   if(mass > AstroPhysics.planetToGasGiantConversionLimit()) {
     //       this.replaceBy(new GasGiant(engine, kin, mass, radius, RandomGasGiantTextureID));
     //   } else {
        super.setMass(mass);
     //   }
    }
}
