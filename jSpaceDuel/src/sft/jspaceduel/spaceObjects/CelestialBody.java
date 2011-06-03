/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sft.jspaceduel.spaceObjects;

import sft.jspaceduel.util.AstroPhysics;

/**
 *
 * @author michael
 */
public abstract class CelestialBody extends SpaceObject implements GravityCenter, Collider {
    double mass;
    
    public CelestialBody(SpaceObjectManager reg, PosParams posParams, double mass) {
        super(reg, posParams);
    }
    
    @Override
    public double[] getAttractionPerTime(SpaceObject object){
        double[] delta=new double[]{pos[0]-x[0],pos[1]-x[1]};
        double distsquared = delta[0]*delta[0]+delta[1]*delta[1];
        double dist=Math.sqrt(delta[0]*delta[0]+delta[1]*delta[1]);

	double pullPerRadiusGAndTime = this.getMass() / (distsquared * dist);
        
        return new double[]{delta[0]*pullPerRadiusGandTime, delta[1]*pullPerRadiusGandTime};
    }   
    
    public double getMass() {
        return mass;
    }
}
