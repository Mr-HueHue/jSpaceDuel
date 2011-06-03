package sft.jspaceduel.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while(!s.isClosed()) {
                String s = in.readLine();
                if(s != null) {
                    h.recievedData(s);
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }
                
    }
}
