package sft.sftengine.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import sft.sftengine.network.Connection;
import sft.sftengine.network.interfaces.DataHandler;
import sft.sftengine.network.interfaces.Sendable;
import sft.sftengine.network.SocketReciever;
import sft.sftengine.network.interfaces.ConnectionManager;
import sft.sftengine.network.interfaces.DataConnection;
import sft.sftengine.network.queue.DataSender;
import sft.sftengine.network.queue.SendableStorage;
import sft.sftengine.physics.TestPhysObject;

/**
 *
 * @author JJ
 */
public class Client implements DataHandler, DataConnection, ConnectionManager {

    DataSender sen;
    SendableStorage stor;
    
    Socket s;
    Connection c;

    public Client(Socket k) throws IOException {
        s = k;
        c = new Connection(s, this);
        SocketReciever r = new SocketReciever(c, this);
        r.start();
        
        stor = new SendableStorage();
        sen = new DataSender(stor, this);
        sen.startSending();
        System.out.println("Client inited...");
        handleinputs();
        
    }
    
    private void handleinputs() {
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
            boolean a = true;
            while (a) {
            try {
                String l = b.readLine();
                if (l != null) {
                    String[] interp = l.split(",");
                    String name = interp[0];
                    int posx, posy;
                    if (interp.length == 2) {
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

                } else {
                    break;
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
            }
    }

    @Override
    public void sendData(Sendable object) {
        try {
            c.getOutputStream().writeObject(object);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void recievedData(Sendable ob) {
        System.out.println("Client data: " + ob.toString());
    }

    @Override
    public void connectionKilled(Connection s) {
        System.out.println("Client connection killed");
    }
}
