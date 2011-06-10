package sft.sftengine.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author JJ
 */
public class SocketReciever extends Thread {

    Socket s;
    DataHandler h;

    public SocketReciever(Socket s, DataHandler h) {
        this.s = s;
        this.h = h;
    }

    @Override
    public void run() {
        try {
            GZIPInputStream ie = new GZIPInputStream(s.getInputStream());
            ObjectInputStream oi = new ObjectInputStream(ie);
            while (!s.isClosed()) {
                try {
                    Sendable ob = (Sendable)oi.readObject();
                    if (ob != null) {
                        h.recievedData(ob);
                    } else {
                        break;
                    }
                } catch (ClassNotFoundException ex) {
                     // hmm...
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }

    }
}
