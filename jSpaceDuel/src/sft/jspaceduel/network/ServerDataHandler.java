package sft.jspaceduel.network;

/**
 *
 * @author JJ
 */
public class ServerDataHandler implements DataHandler {

    @Override
    public void recievedData(String d) {
        System.out.println("Server data: "+ d);
    }
    
}
