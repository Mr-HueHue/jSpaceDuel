package sft.jspaceduel.physics.spaceobjects;

import sft.jspaceduel.physics.PhysicsEngine;

/**
 *
 * @author JJ
 */
public class Star extends CelestialBody implements LightSource {

    @Override
    public double getRadius() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean collide(SpaceObject object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getLuminosity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
