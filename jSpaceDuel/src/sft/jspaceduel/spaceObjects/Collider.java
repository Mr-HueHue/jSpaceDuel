package sft.jspaceduel.spaceObjects;

/**
 * This interface is implemented by all Objects that absorb (or are absorbed by)
 * other objects they collide with.
 * Only SpaceObjects that implement this interface are checked for collisions.
 * 
 * @author michael
 */
public interface Collider {    
    /*
     * This method checks if this object collides with an other SpaceObject.
     */
    public boolean CollidesWith(SpaceObject object);
    
    /*
     * This method is called if an object collides with this object.
     */
    public boolean Collide(SpaceObject object);
}
