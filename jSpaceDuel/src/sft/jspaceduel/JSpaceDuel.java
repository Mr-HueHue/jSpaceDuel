package sft.jspaceduel;

import sft.jspaceduel.game.JSpaceDuelManager;
import org.lwjgl.LWJGLException;



/**
 * Main class of jSpaceDuel.
 * 
 * Documentation at http://wiki.jonasjelten.de/wiki/index.php/JSpaceDuel
 * 
 * @author Jonas Jelten
 */
public class JSpaceDuel {

    public static void main(String[] args) throws LWJGLException {
        JSpaceDuelManager m = new JSpaceDuelManager();
        m.start();
    }
}
