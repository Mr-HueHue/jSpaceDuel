package sft.sftengine.network;

import sft.sftengine.network.interfaces.DataHandler;
import sft.sftengine.network.interfaces.Sendable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.zip.GZIPInputStream;
import sft.sftengine.network.interfaces.ConnectionManager;

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
            ObjectInputStream is = s.getInputStream();
            while (!s.getSocket().isClosed()) {
                try {
                    Sendable ob = (Sendable) is.readObject();
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
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }

    }
}
