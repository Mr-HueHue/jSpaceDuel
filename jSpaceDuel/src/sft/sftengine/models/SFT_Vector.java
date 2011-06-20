/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sft.sftengine.models;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JJ
 */
/**
 * A 3D vector, with functions to perform common vector math operations.
 */
public class SFT_Vector {

    public float x = 0;
    public float y = 0;
    public float z = 0;

    /**
     * Create a 0,0,0 vector 
     */
    public SFT_Vector() {
    }

    /**
     * Create a vector with the given xyz values
     */
    public SFT_Vector(float xpos, float ypos, float zpos) {
        x = xpos;
        y = ypos;
        z = zpos;
    }

    /**
     * Create a vector from the given float[3] xyz values
     */
    public SFT_Vector(float[] float3) {
        x = float3[0];
        y = float3[1];
        z = float3[2];
    }

    /**
     * Create a vector that duplicates the given vector
     */
    public SFT_Vector(SFT_Vector v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    /**
     * Create a vector from point1 to point2
     */
    public SFT_Vector(SFT_Vector point1, SFT_Vector point2) {
        x = point1.x - point2.x;
        y = point1.y - point2.y;
        z = point1.z - point2.z;
    }

    //========================================================================
    // Functions that operate on the vector (change the value of this vector)
    // These functions return "this", so can be chained together:
    //        GL_Vector a = new GLVector(b).mult(c).normalize()
    //========================================================================
    /**
     * Add a vector to this vector
     */
    public SFT_Vector add(SFT_Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    /**
     * Subtract vector from this vector
     */
    public SFT_Vector sub(SFT_Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    /**
     * Multiply this vector by another vector
     */
    public SFT_Vector mult(SFT_Vector v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        return this;
    }

    /**
     * Divide this vector by another vector
     */
    public SFT_Vector div(SFT_Vector v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
        return this;
    }

    /**
     * Add a value to this vector
     */
    public SFT_Vector add(float n) {
        x += n;
        y += n;
        z += n;
        return this;
    }

    /**
     * Subtract a value from this vector
     */
    public SFT_Vector sub(float n) {
        x -= n;
        y -= n;
        z -= n;
        return this;
    }

    /**
     * Multiply vector by a value
     */
    public SFT_Vector mult(float n) {
        x *= n;
        y *= n;
        z *= n;
        return this;
    }

    /**
     * Divide vector by a value
     */
    public SFT_Vector div(float n) {
        x /= n;
        y /= n;
        z /= n;
        return this;
    }

    /**
     * Normalize the vector (make its length 0).
     */
    public SFT_Vector normalize() {
        float len = length();
        if (len == 0) {
            return this;
        }
        float invlen = 1f / len;
        x *= invlen;
        y *= invlen;
        z *= invlen;
        return this;
    }

    /**
     * Reverse the vector
     */
    public SFT_Vector reverse() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Return the length of the vector.
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Return a string representation of the vector
     */
    @Override
    public String toString() {
        return "<vector x=" + x + " y=" + y + " z=" + z + ">";
    }

    /**
     * Return a copy of the vector
     */
    public SFT_Vector getClone() {
        return new SFT_Vector(x,y,z);
    }

    /**
     * Return true if this vector has the same xyz values as the argument vector
     */
    public boolean equals(SFT_Vector v) {
        return (v.x == x && v.y == y && v.z == z);
    }

    //==================================================================
    // Functions that perform binary operations (add, subtract, multiply
    // two vectors and return answer in a new vector)
    //==================================================================
    /**
     * Return a+b as a new vector
     */
    public static SFT_Vector add(SFT_Vector a, SFT_Vector b) {
        return new SFT_Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Return a-b as a new vector
     */
    public static SFT_Vector sub(SFT_Vector a, SFT_Vector b) {
        return new SFT_Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /**
     * Return a*b as a new vector
     */
    public static SFT_Vector mult(SFT_Vector a, SFT_Vector b) {
        return new SFT_Vector(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    /**
     * Return a/b as a new vector
     */
    public static SFT_Vector div(SFT_Vector a, SFT_Vector b) {
        return new SFT_Vector(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    /**
     * Return the given vector multiplied by the given numeric value, as a new vector
     */
    public static SFT_Vector multiply(SFT_Vector v, float r) {
        return new SFT_Vector(v.x * r, v.y * r, v.z * r);
    }

    /**
     * Return a new vector scaled by the given factor
     */
    public static SFT_Vector scale(SFT_Vector a, float f) {
        return new SFT_Vector(f * a.x, f * a.y, f * a.z);
    }

    /**
     * Return the length of the given vector
     */
    public static float length(SFT_Vector a) {
        return (float) Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
    }

    /**
     *  return the dot product of two vectors
     */
    public static float dotProduct(SFT_Vector u, SFT_Vector v) {
        return u.x * v.x + u.y * v.y + u.z * v.z;
    }

    /**
     * Return the normalized vector as a new vector
     */
    public static SFT_Vector normalize(SFT_Vector v) {
        float len = v.length();
        if (len == 0) {
            return v;
        }
        float invlen = 1f / len;
        return new SFT_Vector(v.x * invlen, v.y * invlen, v.z * invlen);
    }

    /**
     * Return the cross product of the two vectors, as a new vector.  The returned vector is 
     * perpendicular to the plane created by a and b.
     */
    public static SFT_Vector crossProduct(SFT_Vector a, SFT_Vector b) {
        return vectorProduct(a, b).normalize();
    }

    /**
     * returns the normal vector of the plane defined by the a and b vectors
     */
    public static SFT_Vector getNormal(SFT_Vector a, SFT_Vector b) {
        return vectorProduct(a, b).normalize();
    }

    /**
     * returns the normal vector from the three vectors
     */
    public static SFT_Vector getNormal(SFT_Vector a, SFT_Vector b, SFT_Vector c) {
        return vectorProduct(a, b, c).normalize();
    }

    /**
     * returns a x b
     */
    public static SFT_Vector vectorProduct(SFT_Vector a, SFT_Vector b) {
        return new SFT_Vector(a.y * b.z - b.y * a.z, a.z * b.x - b.z * a.x, a.x * b.y - b.x * a.y);
    }

    /**
     * returns (b-a) x (c-a)
     */
    public static SFT_Vector vectorProduct(SFT_Vector a, SFT_Vector b, SFT_Vector c) {
        return vectorProduct(sub(b, a), sub(c, a));
    }

    /**
     * returns the angle between 2 vectors
     */
    public static float angle(SFT_Vector a, SFT_Vector b) {
        a.normalize();
        b.normalize();
        return (a.x * b.x + a.y * b.y + a.z * b.z);
    }

    /**
     *  returns the angle between 2 vectors on the XZ plane.
     *  angle is 0-360 where the 0/360 divide is directly in front of the A vector
     *  Ie. when A is pointing directly at B, angle will be 0.
     *  If B moves one degree to the right, angle will be 1,
     *  If B moves one degree to the left, angle will be 360.
     *
     *  right side is 0-180, left side is 360-180
     */
    public static float angleXZ(SFT_Vector a, SFT_Vector b) {
        a.normalize();
        b.normalize();
        return (float) ((Math.atan2(a.x * b.z - b.x * a.z, a.x * b.x + a.z * b.z) + Math.PI) * (Math.PI / 180));
    }

    /**
     *  returns the angle between 2 vectors on the XY plane.
     *  angle is 0-360 where the 0/360 divide is directly in front of the A vector
     *  Ie. when A is pointing directly at B, angle will be 0.
     *  If B moves one degree to the right, angle will be 1,
     *  If B moves one degree to the left, angle will be 360.
     *
     *  right side is 0-180, left side is 360-180
     */
    public static float angleXY(SFT_Vector a, SFT_Vector b) {
        a.normalize();
        b.normalize();
        return (float) ((Math.atan2(a.x * b.y - b.x * a.y, a.x * b.x + a.y * b.y) + Math.PI) * (180 / Math.PI));
    }

    /**
     * return a vector rotated the given number of degrees around the Y axis  
     */
    public static SFT_Vector rotationVector(float degrees) {
        return new SFT_Vector((float) Math.sin(degrees * (Math.PI / 180)),0,(float) Math.cos(degrees * (Math.PI / 180)));
    }
}