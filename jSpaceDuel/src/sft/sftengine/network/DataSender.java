package sft.sftengine.network;


/**
 *
 * @author JJ
 */
public class DataSender extends Thread {

    SendableStorage s;
    DataConnection c;
    boolean sending;

    public DataSender(SendableStorage s, DataConnection c) {
        this.s = s;
        this.c = c;
        sending = true;

    }

    public void startSending() {
        if (!sending) {
            this.start();
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
        while (sending) {
            if (this.isInterrupted()) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                    ex.printStackTrace(System.err);
                }
            }
            try {
                sleep(5000); //TODO: dynamic waiting so that we actually send every whatever seconds
            } catch (InterruptedException ex) {
                System.out.println(ex);
                ex.printStackTrace(System.err);
            }
            /*
             *  SEND NOW
             */
            System.out.println("Sending now");
            c.sendData(s.getNext());
        }

    }
}
