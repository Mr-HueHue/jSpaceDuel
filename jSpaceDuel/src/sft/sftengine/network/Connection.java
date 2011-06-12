/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sft.sftengine.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import sft.sftengine.network.interfaces.ConnectionManager;
import sft.sftengine.network.interfaces.Sendable;

/**
 *
 * @author JJ
 */
public class Connection {

    ObjectOutputStream os;
    ObjectInputStream is;
    Socket s;
    ConnectionManager m;

    public Connection(Socket s, ConnectionManager m) throws IOException {
        this.s = s;
        try {
            os = new ObjectOutputStream(new GZIPOutputStream(s.getOutputStream()));

            is = new ObjectInputStream(new GZIPInputStream(s.getInputStream()));
        } catch (SocketException ex) {
            m.connectionKilled(this);
        }
    }

    public void send(Sendable s) throws IOException {
        os.writeObject(s);
    }

    public ObjectInputStream getInputStream() {
        return is;
    }

    public ObjectOutputStream getOutputStream() {
        return os;
    }

    public Socket getSocket() {
        return s;
    }
}
