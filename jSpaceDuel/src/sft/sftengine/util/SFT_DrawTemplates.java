package sft.sftengine.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 *
 * @author JJ
 */
public class SFT_DrawTemplates {
    /**
     * Draw a rectangle outline in ortho mode (draws in 2D over the scene).
     * <BR>
     * @see setLineWidth()
     * @see drawRectZ()
     */
    public static void drawRect(int x, int y, float w, float h) {
        // switch projection to 2D mode
        SFT_Util.setOrthoOn();
        // draw rectangle at Z=0
        drawRectZ(x,y,0,w,h);
        // restore the previous perspective and model views
        SFT_Util.setOrthoOff();
    }

    /**
     * Draw a rectangle outline in world space.  Uses opengl line_strip to make
     * the rectangle.
     * <BR>
     * @see setLineWidth()
     * @see drawRect()
     */
    public static void drawRectZ(int x, int y, int z, float w, float h) {
    	// preserve current settings
    	GL11.glPushAttrib(GL11.GL_TEXTURE_BIT | GL11.GL_LIGHTING_BIT);
        // de-activate texture and light
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        // draw the rectangle
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glVertex3f( (float)x, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y+h, (float)z);
            GL11.glVertex3f( (float)x, (float)y+h, (float)z);
            GL11.glVertex3f( (float)x, (float)y, (float)z);
        }
        GL11.glEnd();
        // draw points at the corners
        GL11.glBegin(GL11.GL_POINTS);
        {
            GL11.glVertex3f( (float)x, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y+h, (float)z);
            GL11.glVertex3f( (float)x, (float)y+h, (float)z);
        }
        GL11.glEnd();
        // re-enable settings
        SFT_Util.popAttrib();
    }

    /**
     * Draws a circle with the given radius centered at the given world position.
     */
    public static void drawCircleI(int x, int y, int radius, int linewidth) {
        // switch projection to 2D mode
        SFT_Util.setOrthoOn();
        // draw circle at x,y with z=0
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(x,y,0);
            drawCircleF(radius-linewidth, radius, 180);
        }
        GL11.glPopMatrix();
        // restore the previous perspective and model views
        SFT_Util.setOrthoOff();
    }

    /**
     * Draws a circle with the given radius centered at the given world position.
     */
    public static void drawCircleZ(int x, int y, int z, int radius, int linewidth) {
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(x,y,z);
            drawCircleF(radius-linewidth, radius, 180);
        }
        GL11.glPopMatrix();
    }

    /**
     * Draws a circle centered at 0,0,0.  Use translate() to place circle at desired coords.
     * Inner and outer radius specify width, stepsize is number of degrees for each segment.
     */
    public static void drawCircleF(float innerRadius, float outerRadius, int numSegments) {
        int s = 0;     // start
        int e = 360;   // end
        int stepSize = 360/numSegments;   // degrees per segment
        GL11.glBegin(GL11.GL_QUAD_STRIP);
        {
            // add first 2 vertices
            float ts = (float) Math.sin(Math.toRadians(s));
            float tc = (float) Math.cos(Math.toRadians(s));
            GL11.glVertex2f(tc * innerRadius, ts * innerRadius);
            GL11.glVertex2f(tc * outerRadius, ts * outerRadius);
            // add intermediate vertices, snap to {step} degrees
            while ( (s = ( (s + stepSize) / stepSize) * stepSize) < e) {
                ts = (float) Math.sin(Math.toRadians(s));
                tc = (float) Math.cos(Math.toRadians(s));
                GL11.glVertex2f(tc * innerRadius, ts * innerRadius);
                GL11.glVertex2f(tc * outerRadius, ts * outerRadius);
            }
            // add last 2 vertices at end angle
            ts = (float) Math.sin(Math.toRadians(e));
            tc = (float) Math.cos(Math.toRadians(e));
            GL11.glVertex2f(tc * innerRadius, ts * innerRadius);
            GL11.glVertex2f(tc * outerRadius, ts * outerRadius);
        }
        GL11.glEnd();
    }

    /**
     * Render a 2 unit cube centered at origin.  Includes texture coordinates
     * and normals.
     */
    public static void renderCube()
    {
        GL11.glBegin(GL11.GL_QUADS);
        // Front Face
        GL11.glNormal3f( 0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Left
        // Back Face
        GL11.glNormal3f( 0.0f, 0.0f, -1.0f);
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Left
        // Top Face
        GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        // Bottom Face
        GL11.glNormal3f( 0.0f, -1.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        // Right face
        GL11.glNormal3f( 1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        // Left Face
        GL11.glNormal3f( -1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        GL11.glEnd();
    }

    /**
     * draw a cube with the given size, centered at origin.  Include texture coordinates.
     * @param size       length of each side
     * @param segments   # segments to divide each side into
     */
    public static void renderCube(float size, int segments) {
    	float halfsize = size/2f;
    	GL11.glPushMatrix();
    	{
    		GL11.glPushMatrix();
    		{
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// front
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(90,0,1,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// right
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(180,0,1,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// back
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(270,0,1,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// left
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(90,1,0,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// bottom
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(-90,1,0,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// top
    		}
    		GL11.glPopMatrix();
    	}
    	GL11.glPopMatrix();
    }

    /**
     * draw a square plane in the X,Y axis, centered at origin.  Include texture coordinates.
     * @param size       length of each side
     * @param segments   number of segments to divide each side into
     */
    public static void renderPlane(float size, int segments) {
        renderPlane(size,size,segments,segments);
    }

    /**
     * draw a rectangular plane in the X,Y axis, centered at origin, with the specified size and
     * number of divisions.  Texture will cover entire rectangle without repeating.
     * @param length     length of X axis side
     * @param height     length of Y axis side
     * @param segments   number of segments to divide each side into
     */
    public static void renderPlane(float length, float height, int length_segments, int height_segments) {
        renderPlane(length, height, length_segments, height_segments, 1, 1);
    }

    /**
     * draw a rectangular plane in the X,Y axis, centered at origin.  Include texture coordinates.
     * Scale the UV coordinates to same proportion as plane dimensions. Texture will repeat as
     * specified by the tilefactorU and tilefactorV values.  If tilefactor values are 1, the texture
     * will cover the rectangle without tiling.
     * @param length    length on X axis
     * @param depth     length on Y axis
     * @param segments  number of segments to divide each side into
     */
    public static void renderPlane(float length, float height, int length_segments, int height_segments, float tilefactorU, float tilefactorV) {
    	float xpos = - length/2f;
    	float ypos = - height/2f;
    	float segsizeL = length/(float)length_segments;
    	float segsizeH = height/(float)height_segments;
        float maxDimension = (length > height)? length : height;
    	float uvsegsizeL = (length/maxDimension) / (float)length_segments;
    	float uvsegsizeH = (height/maxDimension) / (float)height_segments;
    	GL11.glBegin(GL11.GL_QUADS); {
    		GL11.glNormal3f(0f, 0f, 1f);   // plane is facing up the Z axis
    		for (int x=0; x < length_segments; x++, xpos+=segsizeL) {
    			for (int y=0; y < height_segments; y++, ypos+=segsizeH) {
    				// bottom left
    				GL11.glTexCoord2f((x*uvsegsizeL)*tilefactorU, (y*uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos, ypos, 0f);
    				// bottom rite
    				GL11.glTexCoord2f(((x*uvsegsizeL)+uvsegsizeL)*tilefactorU, (y*uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos+segsizeL, ypos,  0f);
    				// top rite
    				GL11.glTexCoord2f(((x*uvsegsizeL)+uvsegsizeL)*tilefactorU, ((y*uvsegsizeH)+uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos+segsizeL,  ypos+segsizeH, 0f);
    				// top left
    				GL11.glTexCoord2f((x*uvsegsizeL)*tilefactorU, ((y*uvsegsizeH)+uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos,  ypos+segsizeH, 0f);
    			}
    			ypos = - height/2f; // reset column position
    		}
    	}
    	GL11.glEnd();
    }

    /**
     * draw a rectangular plane in the X,Y axis, centered at origin.  Include texture coordinates.
     * Scale the UV coordinates to same proportion as plane dimensions.
     * @param length    length on X axis
     * @param depth     length on Y axis
     * @param segments  number of segments to divide each side into
     */
    public static void renderPlaneORIG(float length, float height, int length_segments, int height_segments) {
    	float xpos = - length/2f;
    	float ypos = - height/2f;
    	float segsizeL = length/(float)length_segments;
    	float segsizeH = height/(float)height_segments;
        float maxDimension = (length > height)? length : height;
    	float uvsegsizeL = (length/maxDimension) / (float)length_segments;
    	float uvsegsizeH = (height/maxDimension) / (float)height_segments;
    	GL11.glBegin(GL11.GL_QUADS); {
    		GL11.glNormal3f(0f, 0f, 1f);   // plane is facing up the Z axis
    		for (int x=0; x < length_segments; x++, xpos+=segsizeL) {
    			for (int y=0; y < height_segments; y++, ypos+=segsizeH) {
    				// bottom left
    				GL11.glTexCoord2f(x*uvsegsizeL, y*uvsegsizeH);
    				GL11.glVertex3f( xpos, ypos, 0f);
    				// bottom rite
    				GL11.glTexCoord2f((x*uvsegsizeL)+uvsegsizeL, y*uvsegsizeH);
    				GL11.glVertex3f( xpos+segsizeL, ypos,  0f);
    				// top rite
    				GL11.glTexCoord2f((x*uvsegsizeL)+uvsegsizeL, (y*uvsegsizeH)+uvsegsizeH);
    				GL11.glVertex3f( xpos+segsizeL,  ypos+segsizeH, 0f);
    				// top left
    				GL11.glTexCoord2f(x*uvsegsizeL, (y*uvsegsizeH)+uvsegsizeH);
    				GL11.glVertex3f( xpos,  ypos+segsizeH, 0f);
    			}
    			ypos = - height/2f; // reset column position
    		}
    	}
    	GL11.glEnd();
    }

    /**
     * call the LWJGL Sphere class to draw sphere geometry
     * with texture coordinates and normals
     * @param facets  number of divisions around longitude and latitude
     */
    public static void renderSphere(int facets) {
        Sphere s = new Sphere();            // an LWJGL class
        s.setOrientation(GLU.GLU_OUTSIDE);  // normals point outwards
        s.setTextureFlag(true);             // generate texture coords
        GL11.glPushMatrix();
        {
	        GL11.glRotatef(-90f, 1,0,0);    // rotate the sphere to align the axis vertically
	        s.draw(1, facets, facets);              // run GL commands to draw sphere
        }
        GL11.glPopMatrix();
    }

    /**
     * draw a sphere with 48 facets (pretty smooth) with normals and texture coords
     */
    public static void renderSphere() {
        renderSphere(48);
    }


    /**
     * Sets glLineWidth() and glPointSize() to the given width.  This will
     * affect geometry drawn using glBegin(GL_LINES), GL_LINE_STRIP, and GL_POINTS.
     * May only work with widths up to 10 (depends on hardware).
     */
    public static void setLineWidth(int width)
    {
    	GL11.glLineWidth(width);
    	GL11.glPointSize(width);
    	//GL11.glEnable(GL11.GL_POINT_SMOOTH);
    	//GL11.glEnable(GL11.GL_LINE_SMOOTH);
    }
    
    
    
    /**
     * Another circle method, pretty acurate
     * 
     * @param center_x
     * @param center_y
     * @param radius 
     */
    public static void drawFilledCircleD(double center_x, double center_y, double radius) {
        double x, y;

        GL11.glBegin(GL11.GL_TRIANGLE_FAN); //Begin Polygon coordinates
        for (double theta = 0; theta < (2 * Math.PI); theta += (2 * Math.PI) / 360) {
            x = center_x + (Math.cos(theta) * radius);
            y = center_y + (Math.sin(theta) * radius);
            GL11.glVertex2d(x, y);
        }
        GL11.glEnd();
    }
}
