package sft.sftengine.network.queue;

import java.util.LinkedList;
import sft.sftengine.network.interfaces.Sendable;

/**
 *
 * @author JJ
 */
public class DataQueue {
    LinkedList<Sendable> q;
    
    public DataQueue() {
        q = new LinkedList<Sendable>();
    }
    
    
    public void add(Sendable s) {
        q.addLast(s);
    }
    
    public Sendable getNext() {
        return q.poll();
    }
    
    public boolean isEmpty() {
        return q.size() == 0;
    }
    
    public int getSize() {
        return q.size();
    }
}
