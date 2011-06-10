
package sft.sftengine.network.sendables;

import sft.sftengine.network.Sendable;

/**
 *
 * @author JJ
 */
public class ChatMessage implements Sendable {
    String msg;
    
    public ChatMessage(String text) { // kein user angegeben, der wird vom server eruiert und dann richtig hingeschrieben
        this.msg = text;
    }
    
    public String getText() {
        return msg;
    }
}
