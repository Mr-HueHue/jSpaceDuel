package sft.sftengine.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        Socket v1;
        try {
            v1 = new Socket("127.0.0.1", 1337);
            v1.setKeepAlive(true);
            Client c1 = new Client(v1);
            
            BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
            boolean a = true;
            while(a) {
                String l = b.readLine();
                if(l != null) {
                    c1.sendData(null);
                    
                } else {
                    break;
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace(System.err);
        }
    }
}
