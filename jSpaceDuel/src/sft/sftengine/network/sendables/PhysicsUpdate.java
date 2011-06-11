package sft.sftengine.network.sendables;

import java.util.ArrayList;
import sft.sftengine.network.Sendable;
import sft.sftengine.physics.PhysicsObject;

/**
 *
 * @author JJ
 */
public class PhysicsUpdate implements Sendable {
    public ArrayList<PhysicsObject> elements;
    
    public PhysicsUpdate() {
        elements = new ArrayList<PhysicsObject>();
    }
    
    public ArrayList<PhysicsObject> getAll() {
        return elements;
    }
}
