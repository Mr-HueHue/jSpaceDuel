package sft.jspaceduel.exception;

/**
 *
 * @author JJ
 */
public class JImageNotFound extends JSpaceDuelException {
    public JImageNotFound(String filepath) {
        super("Image not found: "+ filepath);
    }
}
