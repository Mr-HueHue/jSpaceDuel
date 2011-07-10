package sft.jspaceduel.physics.spaceobjects;

import java.io.Serializable;
import sft.jspaceduel.physics.Kinematics;
import sft.jspaceduel.physics.PhysicsEngine;

/**
 *
 * @author JJ, mic_e
 */
public abstract class SpaceObject implements Serializable {
    /**
     * This variable contains the object's creation game time.
     */
    public final long creationTime;
    
    /**
     * This variable contains the object's kinematic parameters. Those are the
     * position, rotation angle, their time derivates and their current second
     * time derivates that will be applied at the next tick.
     */
    protected Kinematics kin;
    
    /**
     * This is a reference to the game's physics engine this object is registered
     * in.
     */
    public PhysicsEngine engine;
    
    /**
     * This variable contains the object's globally unique ID.
     * This ID is used to identify the object i.e. when synchronizing objects
     * via Network. It can also be used as a source when deciding about the
     * object's texture.
     */
    public final long id;
       
    /**
     * This is the space object's main constructor.
     * It creates the object, registers it with the given physics engine and
     * sets it's initial kinematic parameters.
     * It also sets the object's unique ID.
     * @param engine A referrer to the physics engine.
     * @param kin The object's kinetic parameters.
     */
    public SpaceObject(PhysicsEngine engine, Kinematics kin) {
        this.engine = engine;
        this.kin = kin;
        creationTime = engine.getGameTime();
        id=engine.registerObject(this);
    }
    
    /**
     * This method is called to remove this object from the physics engine.
     * It will then be deleted by the garbage collector.
     */
    public void drop() {
        engine.unregisterObject(this);
    }
    
    /**
     * @return The object's 2D position as double array. Values are given in meters.
     */
    public double[] getPosition() {
        return kin.x;
    }
    
    /**
     * @return The object's current kinematic parameters.
     */
    public Kinematics getKinematics() {
        return kin;
    }

    /**
     * This method accelerates this object. The acceleration will be applied and
     * reset to 0 at the next tick. You can accelerate an object multiple times
     * in the preparation of a tick.
     * @param a The acceleration, measured in m/s²
     */
    public void accelerate(double[] a) {
        kin.accelerate(a);
    }
    
    /** 
     * This method performs a physics tick. Usually, the engine performs ticks
     * regularly in the background (i.e. 1kHz)
     * @param deltaT The game time that has passed since the last tick.
     */
    public void tick(long deltaT) {
        kin.tick(deltaT);
    }
    
    /**
     * This method returns the radius of this object.
     * It is required for rendering and collision checks.
     * @return The object's radius, measured in m.
     */
    public abstract double getRadius();

    /**
     * This method returns the object's mass. Please note we are not
     * talking about rest mass here.
     * The mass of a laser pulse is NOT zero.
     * @return The object's mass, measured in kg.
     */
    public abstract double getMass();
    
    /**
     * This method sets the object's mass to the given amount.
     * @param mass The object's new mass, measured in kg.
     */
    public abstract void setMass(double mass);
    
    /**
     * This method returns the positional delta between this object and the given
     * object.
     * @param object The object to which the positional delta 
     * @return The positional delta in X and Y directions, measured in meters, as a double array. 
     */
    public double[] getPosDelta(SpaceObject object) {
        double[] pos1 = getPosition();
        double[] pos2 = object.getPosition();
        return new double[]{pos1[0]-pos2[0],pos1[1]-pos2[1]};

    }
    
    /**
     * If this method is called, this object absorbs an other object, regarding conservation of
     * mass, momentum and angular momentum.
     * @param object The object that is to be absorbed.
     */
    public void absorb(SpaceObject object) {
        double combinedMass = this.getMass() + object.getMass();
        double invCombinedMass = 1/combinedMass;
        this.kin.v[0] = invCombinedMass*(this.kin.v[0]*this.getMass() + object.kin.v[0]*object.getMass());
        this.kin.v[1] = invCombinedMass*(this.kin.v[1]*this.getMass() + object.kin.v[1]*object.getMass());        
        this.setMass(combinedMass);
        object.drop();
    }
    
    /**
     * This method replaces the object by the given replacement object, regarding conservation of mass,
     * momentum and angular momentum.
     * @param object Replacement object
     */
    public void replaceBy(SpaceObject object) {
        object.setMass(getMass());
        object.kin.x = kin.x;
        object.kin.v = kin.v;
        object.kin.phi = kin.phi;
        object.kin.omega = kin.omega;
        this.drop();
    }
}