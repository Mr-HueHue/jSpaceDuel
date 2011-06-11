package sft.sftengine.network;

/**
 *
 * @author JJ
 */
public class SendableStorage {
    DataQueue q, qhigh;
    
    public SendableStorage() {
        q = new DataQueue();
    }
    
    public void add(Sendable s) {
        add(s, false);
    }
    
    public void add(Sendable s, boolean highPriority) {
        if(highPriority) {
            qhigh.add(s);
        } else {
            q.add(s);
        }
    }
    
    public Sendable getNext() {
        Sendable h = qhigh.getNext();
        if(h == null) {
            h= q.getNext();
        }
        return h;
    }
}
