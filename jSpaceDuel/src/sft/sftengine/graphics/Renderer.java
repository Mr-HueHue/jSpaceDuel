package sft.sftengine.graphics;

/**
 * Classes that implement Renderer will be able to render scenes
 * of the SFTEngineWindow
 * 
 * @see SFTEngineWindow
 * @author jj
 */
public interface Renderer {
    public void render();
    public void init();
    public void update(long milliSecondsSinceLastFrame);
    public void changeResolution(int width, int height);
}
