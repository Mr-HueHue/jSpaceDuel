package sft.sftengine.network.queue;

import sft.sftengine.network.interfaces.Sendable;
import sft.sftengine.network.queue.DataQueue;
import sft.sftengine.network.queue.DataSender;

/**
 *
 * @author JJ
 */
public class SendableStorage {

    DataQueue q, qhigh;
    DataSender s;

    public SendableStorage() {
        q = new DataQueue();
        qhigh = new DataQueue();
    }

    public void add(Sendable se) {
        add(se, false);
    }

    public void add(Sendable se, boolean highPriority) {
        if (highPriority) {
            qhigh.add(se);
            if (s != null) {
                s.report(true);
            }
        } else {
            q.add(se);
            if (s != null) {
                s.report(false);
            }
        }
    }

    public Sendable getNext() {
        Sendable h = qhigh.getNext();
        if (h == null) {
            h = q.getNext();
        }
        return h;
    }

    public boolean isEmpty() {
        return (q.isEmpty() && qhigh.isEmpty());
    }

    public int getSize() {
        return q.getSize() + qhigh.getSize();
    }

    public void setDataSender(DataSender s) {
        this.s = s;
    }
}
