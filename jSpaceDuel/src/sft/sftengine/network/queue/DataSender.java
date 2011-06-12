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

    public DataSender(SendableStorage s, DataConnection c) {
        s.setDataSender(this);
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
        System.out.println("Senderthread started.");
        //boolean sentsomething = false;
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
            

            // Send NOW
            

            Sendable se = s.getNext();
            if (se != null) {
                System.out.println("Sending now, items in queue: "+s.getSize());
                c.sendData(se);
                //sentsomething = true;
            } else {
                //System.out.println("Nothing to send.");
                //sentsomething = false;
            }
            
            try {
                synchronized (this) {
                    
                    //System.out.println("Waiting 10 seconds..");
                        this.wait(10000); //TODO: dynamic waiting so that we actually send every whatever seconds
                    //System.out.println("kk done waiting");
                    
                        
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
                ex.printStackTrace(System.err);
            }
        }
        
        System.out.println("Sending Thread finished.");
    }

    public void report(boolean highpriority) {
        synchronized (this) {
            if (highpriority) {
                this.notify();
            } else {
                // maybe nothing, wait for next tick.
                
            }
        }
    }
}
