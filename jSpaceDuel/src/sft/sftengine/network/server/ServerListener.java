package sft.sftengine.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author JJ
 */
public class ServerListener extends Thread {
    ServerSocket s;
    ServerManager m;
    boolean listening = true;
    public ServerListener(ServerSocket s, ServerManager m) {
        this.setName("Server-Socket-Listener");
        this.s = s;
        this.m = m;
    }
    
    public void stopListening() {
        try {
            listening = false;
            s.close();
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }
    }
    
    public void startListening() {
        listening = true;
        this.start();
    }
    
    @Override
    public void run() {
        while(listening) {
            try {
                Socket i = s.accept();
                i.setKeepAlive(true);
                m.incomingConnection(i);
            } catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace(System.err);
            }
        }
    }
}
