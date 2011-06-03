package sft.jspaceduel.network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author JJ
 */
public class Client implements DataHandler{

    Socket s;
    PrintStream o;

    public Client(Socket k) throws IOException {
        s = k;
        SocketReciever r = new SocketReciever(s, this);
        r.start();
        o = new PrintStream(s.getOutputStream());
    }

    public void sendData(String txt) {
        o.println(txt);
    }
    
    @Override
    public void recievedData(String d) {
        System.out.println("Client data: "+ d);
    }
}
