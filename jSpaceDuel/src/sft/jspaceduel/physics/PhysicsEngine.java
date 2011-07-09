package sft.jspaceduel.physics;

import sft.jspaceduel.physics.spaceobjects.Collider;
import sft.jspaceduel.physics.spaceobjects.Attractor;
import sft.jspaceduel.physics.spaceobjects.SpaceObject;
import java.util.HashSet;
import java.util.Set;
import sft.jspaceduel.physics.spaceobjects.LightSource;

/**
 *
 * @author michael
 */
public class PhysicsEngine {
    final long creationNanoTime;
    final long creationGameTime;
    final long nanoSecondsPerGameSecond = 1000000000/(365*24*60); // one year of game time per minute of system time.
    
    private Set<Attractor> attractors;
    private Set<Collider> colliders;
    public Set<LightSource> lightSources;
    public Set<SpaceObject> allObjects;
    
    public PhysicsEngine(long creationGameTime) {
        this.creationGameTime = creationGameTime;
        creationNanoTime = System.nanoTime();
        allObjects = new HashSet<SpaceObject>();
        attractors = new HashSet<Attractor>();
        colliders = new HashSet<Collider>();
        lightSources = new HashSet<LightSource>();
    }
    
    public PhysicsEngine() {
        this(0);
    }
    
    public void registerObject(SpaceObject object) {
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
    }

    public void unregisterObject(SpaceObject object) {
        allObjects.remove(object);
        attractors.remove((Attractor) object);
        colliders.remove((Collider) object);
        lightSources.remove((LightSource) object);
    }
    
    public long getGameTime() {
        return (System.nanoTime() - creationGameTime + creationNanoTime)/nanoSecondsPerGameSecond;
    }
}
