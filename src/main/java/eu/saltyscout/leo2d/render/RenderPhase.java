package eu.saltyscout.leo2d.render;

/**
 * RenderLayers define an order in which groups of Renderers may paint:<br/>
 * {@link RenderPhase#PRE} indicates that this Renderer will paint early, putting it in the background of the entire scene.
 * {@link RenderPhase#DEFAULT} is the default RenderPhase. Most of the scene should be rendered in the default layer.
 * {@link RenderPhase#POST} is a layer mostly for effects that cover the entire scene.
 * {@link RenderPhase#GUI} indicates the scene has been fully rendered, GUI components may now place themselves on top.
 */
public enum RenderPhase {
    PRE, DEFAULT, POST, GUI
}
