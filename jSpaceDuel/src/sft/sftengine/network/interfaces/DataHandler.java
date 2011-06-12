package sft.sftengine.network.interfaces;

/**
 * Interface that must be implemented by Classes that will handle incuming data
 * from objects streams.
 * 
 * @author JJ
 */
public interface DataHandler {
    public void recievedData(Object ob);
}
