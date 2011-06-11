package sft.sftengine.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author JJ
 */
public final class ServerManager implements DataHandler, DataConnection {

    ArrayList<SocketReciever> rlist;
    ArrayList<Socket> slist;
    ArrayList<ObjectOutputStream> olist;
    ServerListener l;
    DataSender sen;
    SendableStorage stor;

    public static void main(String[] args) {
        new ServerManager();
    }

    public ServerManager() {
        stor = new SendableStorage();
        sen = new DataSender(stor, this);

        rlist = new ArrayList<SocketReciever>();
        slist = new ArrayList<Socket>();
        olist = new ArrayList<ObjectOutputStream>();
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
                        // sendToAll(line);
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
            olist.add(new ObjectOutputStream(new GZIPOutputStream(i.getOutputStream())));
        } catch (IOException ex) {
            //whatever
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }

        System.out.println("client connected: number " + connums++);

    }


    /*public void sendToAll(String what) {
    for (PrintStream p : plist) {
    p.println(what);
    }
    }*/
    @Override
    public void recievedData(Object ob) {
        System.out.println(ob.toString());
    }

    @Override
    public void sendData(Sendable data) {
        for (ObjectOutputStream o : olist) {
            try {
                o.writeObject(data);
            } catch (IOException ex) {
                //whatever
                System.out.println(ex);
                ex.printStackTrace(System.err);
            }
        }
    }
}
