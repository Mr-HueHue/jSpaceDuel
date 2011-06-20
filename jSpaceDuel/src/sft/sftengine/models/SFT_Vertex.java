package sft.sftengine.models;

import java.util.ArrayList;

/**
 * Vertex contains an xyz position, and a list of neighboring
 * triangles.  Normals and texture coordinates are stored in the
 * GL_Triangle object, since each face may have different normals and
 * texture coords for the same vertex.
 *
 * The "neighbor" triangle list holds all triangles that contains
 * this vertex.  Not to be confused with the GL_Triangle.neighborsP1,
 * GL_Triangle.neighborsP2, etc. that contain only neighbors that should
 * be smoothed into the given triangle.
 *
 * jun 2006: added makeClone()
 */
public class SFT_Vertex {

    public SFT_Vector pos = new SFT_Vector();  // xyz coordinate of vertex
    public SFT_Vector posS = new SFT_Vector(); // xyz Screen coords of projected vertex
    public int ID;      // index into parent objects vertexData vector
    public ArrayList neighborTris = new ArrayList(); // Neighbor triangles of this vertex

    public SFT_Vertex() {
        pos = new SFT_Vector(0f, 0f, 0f);
    }

    public SFT_Vertex(float xpos, float ypos, float zpos) {
        pos = new SFT_Vector(xpos, ypos, zpos);
    }

    public SFT_Vertex(float xpos, float ypos, float zpos, float u, float v) {
        pos = new SFT_Vector(xpos, ypos, zpos);
    }

    public SFT_Vertex(SFT_Vector ppos) {
        pos = new SFT_Vector(ppos.x, ppos.y, ppos.z);
    }

    /**
     * add a neighbor triangle to this vertex
     */
    void addNeighborTri(SFT_Triangle triangle) {
        if (!neighborTris.contains(triangle)) {
            neighborTris.add(triangle);
        }
    }

    /**
     * clear the neighbor triangle list
     */
    void resetNeighbors() {
        neighborTris.clear();
    }

    public SFT_Vertex makeClone() {
        SFT_Vertex newVertex = new SFT_Vertex();
        newVertex.pos = pos.getClone();
        newVertex.posS = posS.getClone();
        newVertex.ID = ID;
        return newVertex;
    }

    @Override
    public String toString() {
        return "<vertex  x=" + pos.x + " y=" + pos.y + " z=" + pos.z + ">\r\n";
    }
}