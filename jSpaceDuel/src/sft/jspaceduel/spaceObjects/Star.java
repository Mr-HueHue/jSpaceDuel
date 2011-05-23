package sft.jspaceduel.spaceObjects;

/**
 *
 * @author JJ
 */
public class Star extends SpaceObject {
    public double luminosity;
    
    public Star(double[] pos, double[] v, double mass, double angle, double angularVelocity) {
        super(pos, v, mass, angle, angularVelocity);
        calcLuminosity();
    }
    
    public void setMass(double mass) {
        this.mass = mass;
        calcLuminosity();
    }
    
    public double getMass() {
        return mass;
    }
    
    public double getLuminosity(){
        return luminosity;
    }
    
    public double getFlux(double[] pos){
        double deltaX=pos[0]-posX;
        double deltaY=pos[1]-posY;
        return luminosity/((deltaX*deltaX)+(deltaY*deltaY));
    }
       
    private void calcLuminosity() {
        luminosity = Math.pow(mass*1.98290e-23,3.5);
    }  
    
    public double getRadius() {
        return 0;
    }
}
