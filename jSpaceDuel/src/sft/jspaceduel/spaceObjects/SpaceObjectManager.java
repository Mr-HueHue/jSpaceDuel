package sft.jspaceduel.spaceObjects;

import java.util.ArrayList;

/**
 *
 * @author michael
 */
public class SpaceObjectManager {
    public ArrayList<SpaceObject> gravityObjects;
    public ArrayList<SpaceObject> collisionObjects;
    public ArrayList<Star> stars;
    
    public SpaceObjectManager() {
        gravityObjects = new ArrayList<SpaceObject>();
        collisionObjects = new ArrayList<SpaceObject>();
        stars = new ArrayList<Star>();
    }
}
