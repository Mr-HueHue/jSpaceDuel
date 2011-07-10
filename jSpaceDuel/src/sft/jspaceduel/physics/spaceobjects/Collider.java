package sft.jspaceduel.physics.spaceobjects;

/**
 * This interface is implemented by all Objects that may absorb or take the master role
 * at interacting with other objects whose collision radius touches their collision radius.
 * 
 * @author michael
 */
public interface Collider {   
    /**
     * This method collides the given object with this object if the given object is in this
     * object's collision radius and this object takes the master role in the collision.
     * @param object The object that is to be collided with.
     * @return true if an collision has occured.
     */
    public boolean collide(SpaceObject object);
}
