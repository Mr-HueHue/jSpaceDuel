package sft.sftengine.network;

import sft.sftengine.network.interfaces.DataHandler;
import sft.sftengine.network.interfaces.Sendable;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author JJ
 */
public class SocketReciever extends Thread {

    Connection s;
    DataHandler h;

    public SocketReciever(Connection s, DataHandler h) {
        this.s = s;
        this.h = h;
    }

    @Override
    public void run() {
        try {
            while (!s.getSocket().isClosed()) {
                try {
                    Sendable ob = (Sendable) s.getInputStream().readObject();
                    if (ob != null) {
                        h.recievedData(ob);
                    } else {
                        break;
                    }
                } catch (ClassNotFoundException ex) {
                    // hmm...
                    System.out.println(ex);
                    ex.printStackTrace(System.err);
                }
            }
            System.out.println("socket reciever connection killed.");
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }

    }
}
