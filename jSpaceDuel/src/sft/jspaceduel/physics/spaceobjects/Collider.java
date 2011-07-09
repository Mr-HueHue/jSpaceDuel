package sft.jspaceduel.physics.spaceobjects;

/**
 * This interface is implemented by all Objects that may absorb or take the master role
 * at interacting with other objects whose collision radius touches their collision radius.
 * 
 * @author michael
 */
public interface Collider {   
    /**
     * This method 
     * @param object
     * @return true if an collision has occured.
     */
    public boolean collide(SpaceObject object);
}
