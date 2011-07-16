package sft.sftengine.network.queue;

import sft.sftengine.network.interfaces.DataConnection;
import sft.sftengine.network.interfaces.Sendable;

/**
 *
 * @author JJ
 */
public class DataSender extends Thread {

    SendableStorage s;
    DataConnection c;
    boolean sending;
    boolean sentsomething;
    int waittime;

    public DataSender(SendableStorage s, DataConnection c, int waittime) {
        s.setDataSender(this);
        this.waittime = waittime;
        this.s = s;
        this.c = c;
        sending = false;
        this.setName("DataSender");
    }

    public void startSending() {
        System.out.println("Starting sender thread...");

        if (!sending) {
            this.start();
        } else {
            synchronized (this) {
                this.notify();
            }
        }
        sending = true;

    }

    public void interruptSending() {
        this.interrupt();
    }

    public void stopSending() {
        sending = false;
        this.interrupt();
    }

    @Override
    public void run() {
        sentsomething = false;
        while (sending) {

            while (this.isInterrupted()) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                    ex.printStackTrace(System.err);
                }
            }

            Sendable se = s.getNext();
            if (se != null) {
                System.out.println("Sending now...");
                c.sendData(se);
                System.out.println("Items in queue: " + s.getSize());
                sentsomething = true;
            } else {
                System.out.println("Nothing to send.");
                sentsomething = false;
            }


            try {
                synchronized (this) {
                    if (sentsomething) {
                        if (s.isEmpty()) {
                            this.wait();
                        } else {
                            this.wait(waittime);
                        }
                    } else {
                        this.wait();
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
                ex.printStackTrace(System.err);
            }
        }

        System.out.println("Sending Thread finished.");
    }

    /**
     * method that is called by the connection manager, nottifies this object to stop waiting.
     * @param highpriority 
     */
    public void report(boolean highpriority) {
        synchronized (this) {
            if (highpriority) {
                this.notify();
            } else if (sentsomething) {
                // maybe nothing, wait for next tick.
            } else if (!sentsomething) {
                this.notify();
            }
        }
    }
}
