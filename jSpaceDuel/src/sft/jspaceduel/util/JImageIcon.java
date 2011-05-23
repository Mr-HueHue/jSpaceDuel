package sft.jspaceduel.util;

import java.net.URL;
import javax.swing.ImageIcon;
import sft.jspaceduel.exception.JImageNotFound;

/**
 *
 * @author JJ
 */
public class JImageIcon {
    public JImageIcon i;
    public JImageIcon(String filepath) throws JImageNotFound {
        URL imageurl = getClass().getResource(filepath);
        if(imageurl != null) {
            i = new JImageIcon(imageurl);
        } else {
            throw new JImageNotFound(filepath);
        }
    }
    public JImageIcon get() {
        return i;
    }
}
