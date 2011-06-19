package sft.sftengine.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public Connection(Socket so, ConnectionManager m)  {
        this.s = so;
        createConnection();
    }

    private void createConnection() {
        try {
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            try {
                s.close();
            } catch (IOException ex1) {
                ex1.printStackTrace(System.err);
            }
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
    
    public void writeSendable(Sendable s) throws IOException {
        os.writeObject(s);
    }
            
    
    @Override
    public String toString() {
        return "ip of partner: "+s.getInetAddress().getHostAddress() + " port:" + s.getPort();
    }
            
}
