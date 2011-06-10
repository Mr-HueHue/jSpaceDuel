package sft.sftengine.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author JJ
 */
public class Client implements DataHandler {

    Socket s;
    DataOutputStream o;
    GZIPOutputStream zip;
    ObjectOutputStream obj;

    public Client(Socket k) throws IOException {
        s = k;
        SocketReciever r = new SocketReciever(s, this);
        r.start();
        zip = new GZIPOutputStream(s.getOutputStream());
        o = new DataOutputStream(zip);
        obj = new ObjectOutputStream(zip);
    }

    public void sendData(Sendable object) {
        double yourgun = 8.8;

    }

    @Override
    public void recievedData(Object ob) {
        System.out.println("Client data: " + ob.toString());
    }
}
