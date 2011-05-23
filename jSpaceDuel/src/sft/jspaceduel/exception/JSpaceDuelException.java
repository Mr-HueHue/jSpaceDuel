package sft.jspaceduel.exception;

/**
 *
 * @author JJ
 */
public class JSpaceDuelException extends Exception {
    public JSpaceDuelException() {
        super("Error in JSpaceDuel");
    }
    
    public JSpaceDuelException(String msg) {
        super(msg);
    }
    
    @Override
    public String getLocalizedMessage() {
        return "Fehler in JSpaceDuel";
    }
}
