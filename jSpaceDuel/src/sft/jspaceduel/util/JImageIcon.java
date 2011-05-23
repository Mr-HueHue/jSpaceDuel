package sft.jspaceduel.util;

import java.net.URL;
import javax.swing.ImageIcon;
import sft.jspaceduel.exception.JImageNotFound;

/**
 *
 * @author JJ
 */
public class JImageIcon {
    public ImageIcon i;
    public JImageIcon(String filepath) throws JImageNotFound {
        URL imageurl = getClass().getResource(filepath);
        if(imageurl != null) {
            i = new ImageIcon(imageurl);
        } else {
            throw new JImageNotFound(filepath);
        }
    }
    public ImageIcon get() {
        return i;
    }
}
