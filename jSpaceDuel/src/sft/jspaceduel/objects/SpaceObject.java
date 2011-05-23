package sft.jspaceduel.objects;

import sft.jspaceduel.util.JImageIcon;

/**
 *
 * @author JJ
 */
public class SpaceObject {
    protected double posX;
    protected double posY;
    protected double vX;
    protected double vY;
    protected double mass;
    protected double radius;
    protected double rotationangle;
    /*protected String pictureFileName;
    protected JImageIcon picture;*/
    
    public SpaceObject(double posX, double posY, double vX, double vY, double mass, double radius) {
        this.posX = posX;
        this.posY = posY;
        this.vX = vX;
        this.vY = vY;
        this.mass = mass;
        this.radius = radius;
    }
    
    public void accelerate(double dvX, double dvY) {
        vX += dvX;
        vY += dvY;
    }
    
    public void setPosition(double dposX, double dposY) {
        posX += dposX;
        posY += dposY;
    }
    
}
