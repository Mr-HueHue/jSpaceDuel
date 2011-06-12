package sft.sftengine.physics;

import sft.sftengine.network.interfaces.Sendable;

/**
 *
 * @author JJ
 */
public class TestPhysObject extends PhysicsObject implements Sendable {
    int[] pos;
    String name;
    boolean alive;
    
    public TestPhysObject(String name, int posx, int posy) {
        pos = new int[2];
        pos[0] = posx;
        pos[1] = posy;
        this.name = name;
        alive = true;
    }
    
    public void move(int dx, int dy) {
        pos[0] += dx;
        pos[1] += dy;
    }
    
    public void destroy() {
        alive = false;
    }
    
    @Override
    public String toString() {
        return "PhysObj '"+name+"' at "+pos[0]+","+pos[1]+";";
    }
}
