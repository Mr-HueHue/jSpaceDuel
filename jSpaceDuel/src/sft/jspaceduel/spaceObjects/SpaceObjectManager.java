package sft.jspaceduel.spaceObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sft.jspaceduel.spaceObjects.LightSource;

/**
 *
 * @author michael
 */
public class SpaceObjectManager {
    public Set<GravityCenter> gravityCenters;
    public Set<Collider> absorbers;
    public Set<LightSource> lightSources;
    public Set<SpaceObject> allObjects;
    
    public SpaceObjectManager() {
        gravityCenters = new HashSet<GravityCenter>();
        absorbers = new HashSet<Collider>();
        lightSources = new HashSet<LightSource>();
        allObjects = new HashSet<SpaceObject>();
    }
    
    public void registerObject(SpaceObject object) {
        allObjects.add(object);
        if (GravityCenter.class.isInstance(object)) {
            gravityCenters.add((GravityCenter) object);
        }       
        if (Collider.class.isInstance(object)) {
            absorbers.add((Collider) object);
        }               
        if (LightSource.class.isInstance(object)) {
            lightSources.add((LightSource) object);
        }       
    }
    
    public void unregisterObject() {
        
    } 
}
