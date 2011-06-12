package sft.sftengine.exception;

/**
 *
 * @author JJ
 */
public class SFTEngineException extends Exception {
      public SFTEngineException() {
        super("Error in the SFT-Engine");
    }
    
    public SFTEngineException(String msg) {
        super(msg);
    }
    
    @Override
    public String getLocalizedMessage() {
        return "Allgemeiner Fehler in der SFT-Engine";
    }
}
