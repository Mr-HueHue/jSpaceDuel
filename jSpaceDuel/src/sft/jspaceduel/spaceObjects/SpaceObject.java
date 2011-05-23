package sft.jspaceduel.spaceObjects;

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
    protected double angle;
    protected double angularVelocity;
    /*protected String pictureFileName;
    protected JImageIcon picture;*/
    
    public SpaceObject(double[] pos, double[] v, double mass, double angle, double angularVelocity) {
        this.posX = pos[0];
        this.posY = pos[1];
        this.vX = v[0];
        this.vY = v[1];
        this.mass = mass;
        this.angularVelocity = angularVelocity;
        this.angle = angle;
    }
    
    public void accelerate(double[] dv) {
        vX += dv[0];
        vY += dv[1];
    }
    
    public void move(double[] dpos) {
        posX += dpos[0];
        posY += dpos[1];
    }
    
    public double[] getGravitationalPull(double[] pos){
        double deltaX=pos[0]-posX;
        double deltaY=pos[1]-posY;
        double pullBoth = mass/((deltaX*deltaX)+(deltaY*deltaY));
        return new double[]{pullBoth, pullBoth}; //todo: x und y komponente des pulls berechnen.
    }   
}
