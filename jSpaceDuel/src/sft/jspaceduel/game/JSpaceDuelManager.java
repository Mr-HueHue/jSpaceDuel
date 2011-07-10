package sft.jspaceduel.game;

import java.awt.Dimension;
import org.lwjgl.LWJGLException;
import sft.jspaceduel.graphics.JSpaceDuelRenderer;
import sft.jspaceduel.physics.PhysicsEngine;
import sft.sftengine.graphics.SFTEngineWindow;

/**
 *
 * @author JJ
 */
public class JSpaceDuelManager {
    public PhysicsEngine physics;
    public SFTEngineWindow window;
    JSpaceDuelRenderer renderer;
    int wi = 800, he = 600;
    
    public JSpaceDuelManager() throws LWJGLException {
        physics = new PhysicsEngine();
        renderer = new JSpaceDuelRenderer(this);
        window = new SFTEngineWindow(renderer, wi, he, "JSpaceDuel");
        
        window.enableVSync();
    }
    
    public void changeResolution(int newwi, int newhe) {
        wi = newwi;
        he = newhe;
    }
    
    public Dimension getResolution() {
        return new Dimension(wi, he);
    }
    
    public void start() {
        window.start();
    }
}
