package sft.sftengine.network.sendables;

import java.util.ArrayList;
import sft.sftengine.network.interfaces.Sendable;
import sft.sftengine.physics.PhysicsObject;

/**
 *
 * @author JJ
 */
public class PhysicsTotal implements Sendable {
    
    public ArrayList<PhysicsObject> elements;
    
    public PhysicsTotal() {
        elements = new ArrayList<PhysicsObject>();
    }
    
    public ArrayList<PhysicsObject> getAll() {
        return elements;
    }
    
}
