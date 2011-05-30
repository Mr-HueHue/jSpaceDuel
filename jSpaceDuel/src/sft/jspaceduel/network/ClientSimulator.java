package sft.jspaceduel.network;

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
        Socket v1, v2;
        try {
            v1 = new Socket("127.0.0.1", 1337);
            Client c1 = new Client(v1);
            
            v2 = new Socket("127.0.0.1", 1337);
            Client c2 = new Client(v2);
            
            BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
            boolean a = true;
            while(a) {
                String l = b.readLine();
                if(l != null) {
                    c2.sendData("c2send:" + l);
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
