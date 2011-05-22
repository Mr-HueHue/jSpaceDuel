package sft.jspaceduel.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import sft.jspaceduel.sysinit.Initlibraries;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author JJ
 */
public class test {
    public static void main(String[] args) throws LWJGLException {
        Initlibraries.addlwjgl();
        test test = new test();
    }
    public test() throws LWJGLException {
        
        // init OpenGL here
	this.start();	
	
		
	
    }
    
    public void start() {
        try {
	    Display.setDisplayMode(new DisplayMode(800,600));
	    Display.create();
	} catch (LWJGLException e) {
	    e.printStackTrace();
	    System.exit(0);
	}

	// init OpenGL here
		
	while (!Display.isCloseRequested()) {
	
	    // render OpenGL here
			
	    Display.update();
	}
		
	Display.destroy();
    }
}
