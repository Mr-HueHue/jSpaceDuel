package sft.sftengine.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author JJ
 */
public final class ServerManager implements DataHandler {

    ArrayList<SocketReciever> rlist;
    ArrayList<Socket> slist;
    ArrayList<PrintStream> plist;
    ServerListener l;

    public static void main(String[] args) {
        new ServerManager();
    }

    public ServerManager() {
        rlist = new ArrayList<SocketReciever>();
        slist = new ArrayList<Socket>();
        plist = new ArrayList<PrintStream>();
        try {
            ServerSocket s = new ServerSocket(1337);
            l = new ServerListener(s, this);
            l.startListening();
            System.out.println("Started successfully -.-");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            boolean r = true;
            while (r) {
                String line = in.readLine();
                if (line != null) {
                    // interprete
                    if (line.equals("quit")) {
                        l.stopListening();
                        r = false;
                    } else {
                        // send to all clients
                        sendToAll(line);
                    }
                } else {
                    r = false;
                }
            }
            l.stopListening();
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }


    }
    int connums = 1;

    void incomingConnection(Socket i) {
        SocketReciever r = new SocketReciever(i, this);
        rlist.add(r);
        r.start();

        slist.add(i);

        try {
            // Send welcome msg

            PrintStream sout = new PrintStream(i.getOutputStream());
            plist.add(sout);
            System.out.println("client connected: number " + connums);
            sout.println("welcome " + connums++);
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }
    }
    

    public void sendToAll(String what) {
        for (PrintStream p : plist) {
            p.println(what);
        }
    }

    @Override
    public void recievedData(Object ob) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
