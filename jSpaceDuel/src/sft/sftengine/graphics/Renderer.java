package sft.sftengine.graphics;

/**
 *
 * @author jj
 */
public interface Renderer {
    public void render();
    public void init();
    public void update(long milliSecondsSinceLastFrame);
}
