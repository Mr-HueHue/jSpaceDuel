/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sft.jspaceduel.physics.spaceobjects;

import sft.jspaceduel.physics.Kinematics;
import sft.jspaceduel.physics.PhysicsEngine;
import sft.jspaceduel.physics.AstroPhysics;

/**
 *
 * @author michael
 */
public abstract class CelestialBody extends SpaceObject implements Attractor, Collider {
    double mass;
    
    public CelestialBody(PhysicsEngine reg, Kinematics posParams, double mass) {
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
