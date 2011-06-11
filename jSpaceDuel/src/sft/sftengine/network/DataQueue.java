package sft.sftengine.network;

import java.util.PriorityQueue;

/**
 *
 * @author JJ
 */
public class DataQueue {
    PriorityQueue<Sendable> q;
    
    public DataQueue() {
        q = new PriorityQueue<Sendable>();
    }
    
    
    public void add(Sendable s) {
        q.add(s);
    }
    
    public Sendable getNext() {
        return q.poll();
    }
}
