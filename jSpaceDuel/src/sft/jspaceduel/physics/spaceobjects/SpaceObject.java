package sft.jspaceduel.physics.spaceobjects;

import java.io.Serializable;
import sft.jspaceduel.physics.Kinematics;
import sft.jspaceduel.physics.PhysicsEngine;

/**
 *
 * @author JJ, mic_e
 */
public abstract class SpaceObject implements Serializable {

    private long lastMoved;
    private long lastAccelerated;
    public final long creationTime;
    protected Kinematics kin;
    private PhysicsEngine engine;

    /**
     * This is the space object's constructor.
     * @param engine A referrer to the physics engine.
     * @param kinParams 
     */
    public SpaceObject(PhysicsEngine engine, Kinematics kin) {
        this.engine = engine;
        this.kin = kin;
        creationTime = engine.getGameTime();
        lastMoved = creationTime;
        lastAccelerated = creationTime;
        engine.registerObject(this);
    }
    
    //todo: destructor: reg.unregisterObject(this)

    /**
     * @return The object's current kinematic parameters.
     */
    public Kinematics getKinematics() {
        return kin;
    }

    /**
     * This method moves the object based on it's current velocity vector.
     */
    public void move() {
        long moveTime = engine.getGameTime();
        kin.move(moveTime - lastMoved);
        lastMoved = moveTime;
    }

    /**
     * This method accelerates this object.
     * @param a The acceleration, measured in m/ns²
     */
    public void accelerate(double[] a) {
        long accelerationTime = engine.getGameTime();
        kin.accelerate(accelerationTime - lastAccelerated, a);
        lastAccelerated = accelerationTime;
    }

    /**
     * This method returns the collision radius of this object.
     */
    public abstract double getCollisionRadius();

    /**
     * This method returns the object's mass. Please note we are not
     * talking about rest mass here. The mass of a laser pulse is NOT zero.
     */
    public abstract double getMass();
}