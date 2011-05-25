package sft.jspaceduel.spaceObjects;

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
    
    public void setspeed(double[] dv) {
        vX = dv[0];
        vY = dv[1];
    }

    public void move(double[] dpos) {
        posX += dpos[0];
        posY += dpos[1];
    }

    public double[] getGravitationalPull(double[] pos) {
        double deltaX = pos[0] - posX;
        double deltaY = pos[1] - posY;
        double pullBoth = mass / ((deltaX * deltaX) + (deltaY * deltaY));
        return new double[]{pullBoth, pullBoth}; //todo: x und y komponente des pulls berechnen.
    }

    public double[] getPosition() {
        return new double[]{posX, posY};
    }

    public double[] getVelocity() {
        return new double[]{vX, vY};
    }

    /**
     * Unelastic collision of two spaceobjects
     * @param partner to collide with
     */
    public void collideWith(SpaceObject partner) {
        double m1, m2, v1x, v1y, v2x, v2y;
        boolean partnerleichter;
        if (partner.getMass() <= mass) {
            partnerleichter = true;
            m1 = mass;
            m2 = partner.getMass();
            v1x = vX;
            v1y = vY;
            v2x = partner.getVelocity()[0];
            v2y = partner.getVelocity()[1];
        } else {
            partnerleichter = false;
            m2 = mass;
            m1 = partner.getMass();
            v2x = vX;
            v2y = vY;
            v1x = partner.getVelocity()[0];
            v1y = partner.getVelocity()[1];
        }

        double pX1 = m1 * v1x;
        double pY1 = m1 * v1y;
        double pX2 = m2 * v2x;
        double pY2 = m2 * v2y;

        double pXnew = (pX1 + pX2) / 2;
        double pYnew = (pY1 + pY2) / 2;

        if(partnerleichter) {
            this.setMass(m1+m2);
            partner.destroy();
        } else {
            partner.setMass(m1+m2);
            this.destroy();
        }


    }

    public double getMass() {
        return mass;
    }
    
    public void setMass(double newmass) {
        mass = newmass;
    }

    /**
     * Destroy a space object, with nice bunt animation etc
     */
    public void destroy() {
        
    }
}
