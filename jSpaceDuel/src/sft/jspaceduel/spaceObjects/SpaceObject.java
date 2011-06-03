package sft.jspaceduel.spaceObjects;

/**
 *
 * @author JJ, mic_e
 */
public abstract class SpaceObject {
    private long lastMoved;
    private long lastAccelerated;
    public final long creationTime;
    
    protected PosParams posParams;
    
    /**
     * This is the space object's constructor.
     * @param reg A referrer to the space object registry.
     * @param posParams 
     */
    public SpaceObject(SpaceObjectManager reg, PosParams posParams) {
        this.posParams = posParams;
        creationTime = System.nanoTime();
        lastMoved = creationTime;
        lastAccelerated = creationTime;
        reg.registerObject(this);
    }
    
    /**
     * @return The object's current positional parameters.
     */
    public PosParams getPosParams() {
        return posParams;
    }
    
    /**
     * This method moves the object based on it's current velocity vector.
     */
    public void move() {
        long moveTime = System.nanoTime();
        posParams.move(moveTime - lastMoved);
        lastMoved = moveTime;
    }
    
    /**
     * This method accelerates this object.
     * @param a The acceleration, measured in m/ns²
     */
    public void accelerate(double[] a) {
        long accelerationTime = System.nanoTime();
        posParams.accelerate(accelerationTime - lastAccelerated, a);
        lastAccelerated = accelerationTime;
    }
    
    /**
     * This method returns the collision radius of this object.
     */
    public abstract double getCollisionRadius();
 }
