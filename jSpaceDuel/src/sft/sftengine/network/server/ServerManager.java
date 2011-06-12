package sft.sftengine.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;
import sft.sftengine.network.Connection;
import sft.sftengine.network.interfaces.DataConnection;
import sft.sftengine.network.interfaces.DataHandler;
import sft.sftengine.network.queue.DataSender;
import sft.sftengine.network.interfaces.Sendable;
import sft.sftengine.network.queue.SendableStorage;
import sft.sftengine.network.SocketReciever;
import sft.sftengine.network.interfaces.ConnectionManager;
import sft.sftengine.physics.TestPhysObject;

/**
 *
 * @author JJ
 */
public final class ServerManager implements DataHandler, DataConnection, ConnectionManager {

    ArrayList<SocketReciever> rlist;
    ArrayList<Connection> clist;
    ServerListener l;
    DataSender sen;
    SendableStorage stor;

    public static void main(String[] args) {
        new ServerManager();
    }

    public ServerManager() {
        stor = new SendableStorage();
        sen = new DataSender(stor, this);
        sen.startSending();

        rlist = new ArrayList<SocketReciever>();
        clist = new ArrayList<Connection>();
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
                        String[] interp = line.split(",");
                        String name = interp[0];
                        int posx, posy;
                        if(interp.length == 3) {
                            try {
                                posx = Integer.parseInt(interp[1]);
                                posy = Integer.parseInt(interp[2]);
                            } catch (NumberFormatException ex) {
                                System.out.println("Wrong input. Must be name,posx,posy");
                                continue;
                            }
                        } else {
                            posx = 0;
                            posy = 0;
                        }
                        
                        TestPhysObject to = new TestPhysObject(name, posx, posy);
                        System.out.println("enqueuing "+ to.toString());
                        stor.add(to);
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

    void incomingConnection(Socket i) throws IOException {

        SocketReciever r = new SocketReciever(new Connection(i, this), this);
        rlist.add(r);
        r.start();

        clist.add(new Connection(i, this));

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
        System.out.println("server-datasend invoked");
        for (Connection o : clist) {
            try {
                o.send(data);
            } catch (IOException ex) {
                //whatever
                System.out.println(ex);
                ex.printStackTrace(System.err);
            }
        }
    }

    @Override
    public void connectionKilled(Connection s) {
        clist.remove(s);
        System.out.println("Connection dropped.");
    }
}
