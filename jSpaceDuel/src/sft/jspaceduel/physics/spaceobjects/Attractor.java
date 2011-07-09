/*
 * This interface is implemented by every object that may attract an other
 * object.
 * When calculating object acceleration due to attraction, the engine interates
 * through all objects that implement this interface.
 * Note that this interface can be used for forces other than gravity as well.
 * It also i.e. supports attractors with differing distance laws or ones that do
 * only attract a few selected types of SpaceObjects.
 */
package sft.jspaceduel.physics.spaceobjects;

/**
 *
 * @author michael
 */
public interface Attractor {
    /**
     * This method accelerates the given object and, if the force is not of
     * gravitational nature, itself to fulfill the actio=reactio principle.
     * @param object The object that is to be attracted.
     */
    public void attract(SpaceObject object);
}
