package eu.saltyscout.leo2d.render;

import eu.saltyscout.leo2d.component.Component;
import org.dyn4j.geometry.AABB;

/**
 * Created by Peter on 26.10.2017.
 */
public interface Renderer extends Component {

    /**
     * Indicates during which section of the rendering process this Renderer would like to paint.<br/>
     * See {@link RenderPhase} for more details.
     *
     * @return a RenderPhase.
     */
    RenderPhase getPhase();

    /**
     * Gets the world-space area this renderer intends to draw to. <br/>
     * If there is no intersection between the given AABB and the Camera bounds, the Renderer is culled.<br/>
     * This is ignored if the RenderPhase is {@link RenderPhase#GUI}, which will always paint.
     *
     * @return a AABB representing world-space boundaries.
     */
    AABB getAABB();

    /**
     * This method is called by the Camera.
     * Only during the runtime of paint() may a Renderer draw to the Canvas.
     */
    void paint(int passIndex);

    /**
     * Each Renderer is located in one specific layer. Layers do not affect the Render Order, but allow certain objects to be ignored by the canera.
     *
     * @return
     */
    int getLayer();

    /**
     * The Pass indices are a collection of priority-assigning integers which determine when the Renderer may paint during the selected Phase.<br/>
     * Each Index guarantees one paint pass. A renderer may request any number of render passes.
     *
     * @return a non-null array of integers.
     */
    int[] getPassIndices();
}
