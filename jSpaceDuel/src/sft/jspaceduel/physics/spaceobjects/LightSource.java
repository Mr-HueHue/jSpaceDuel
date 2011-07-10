package sft.jspaceduel.physics.spaceobjects;

/**
 * This interface is implemented by all objects that emit light, like stars.
 * This is necessary i.e. for use as dynamic light sources by the graphics
 * engine, or for calculating the spacecraft's solar panel power.
 * @author michael
 */
public interface LightSource {
    /**
     * This method returns the object's luminosity.
     * @return The object's luminosity, given in watts.
     */
    public double getLuminosity();
}
