package sft.jspaceduel.physics;

import sft.jspaceduel.physics.spaceobjects.Collider;
import sft.jspaceduel.physics.spaceobjects.Attractor;
import sft.jspaceduel.physics.spaceobjects.SpaceObject;
import java.util.HashSet;
import java.util.Set;
import sft.jspaceduel.physics.spaceobjects.LightSource;

/**
 * This is the physics main class.
 * It manages any existing physics objects and stores them in sets.
 * Those sets can then i.e. be used by the graphics or network engine.
 * It furthermore manages the game time.
 * @author michael
 */
public class PhysicsEngine {
    /**
     * This variable counts the unique IDs that objects are assigned to all newly created SpaceObjects.
     */
    long idCounter = 0;
    
    /**
     * This constant contains the system's nanoTime at which this physics engine has been created.
     * It is used as offset for calculating the current game time and set in the constructor.
     */
    final long creationNanoTime;
    
    /**
     * This constant contains the game time at which this physics engine has been created.
     * It is used as offset for calculating the current game time and set in the constructor.
     */
    final long creationGameTime;
    
    /**
     * This constant contains the amount of system time nanoseconds that pass until one game second
     * has passed.
     */
    final long nanoSecondsPerGameSecond = 1000000000/(365*24*60); // one year of game time per minute of system time.
    
    /**
     * This set contains all attractors that exist within this physics engine's world.
     */
    private Set<Attractor> attractors;
    
    /**
     * This set contains all colliders that exist within this physics engine's world.
     */
    private Set<Collider> colliders;
    
    /**
     * This set contains all light sources that exist within this physics engine's world.
     */
    public Set<LightSource> lightSources;
    
    /**
     * This set contains all objects that exist within this physics engine's world.
     */
    public Set<SpaceObject> allObjects;
    
    /**
     * This is the physics engine's main constructor.
     * @param creationGameTime The game time at the point of the engine's initialization.
     * @param idCounter The engine's unique ID counter starting value.
     */
    public PhysicsEngine(long creationGameTime, long idCounter) {
        this.creationGameTime = creationGameTime;
        this.idCounter = idCounter;
        creationNanoTime = System.nanoTime();
        allObjects = new HashSet<SpaceObject>();
        attractors = new HashSet<Attractor>();
        colliders = new HashSet<Collider>();
        lightSources = new HashSet<LightSource>();
    }
    
    /**
     * This constructor will call the main constructor with the default values (0,0),
     * which should be good for the master engine.
     */
    public PhysicsEngine() {
        this(0,0);
    }
    
    /**
     * This method is called in each space object's constructor.
     * It registers the object in all the sets it applies to, and in return assigns it
     * a unique ID.
     * @param object The object that is to be registered.
     */
    public long registerObject(SpaceObject object) {
        allObjects.add(object);
        if (Attractor.class.isInstance(object)) {
            attractors.add((Attractor) object);
        }       
        if (Collider.class.isInstance(object)) {
            colliders.add((Collider) object);
        }               
        if (LightSource.class.isInstance(object)) {
            lightSources.add((LightSource) object);
        } 
        return getUniqueID();
    }

    /**
     * This method unregisters the given object from all sets.
     * The object is then destroyed by the garbage collector since it's not
     * referenced anymore.
     * @param object The object that is to be unregistered.
     */
    public void unregisterObject(SpaceObject object) {
        allObjects.remove(object);
        attractors.remove((Attractor) object);
        colliders.remove((Collider) object);
        lightSources.remove((LightSource) object);
    }
    
    /**
     * @return The current game time, in seconds from creation.
     */
    public long getGameTime() {
        return (System.nanoTime() - creationGameTime + creationNanoTime)/nanoSecondsPerGameSecond;
    }
    
    /**
     * This method generates a unique object ID.
     * @return A unique object ID (Long)
     */
    public long getUniqueID() {
        return idCounter++;
    }
}
