package sft.sftengine.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author JJ
 */
public class ClientSimulator {

    public static void main(String[] args) {
        new ClientSimulator();
    }

    public ClientSimulator() {
        try {
            Socket ieeee = new Socket("127.0.0.1", 1337);
            System.out.println("Connected to " + ieeee.getInetAddress().getHostAddress() + ":"+ ieeee.getPort());
            ieeee.setKeepAlive(true);
            Client nein  = new Client(ieeee);
        } catch (UnknownHostException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }
    }
}
