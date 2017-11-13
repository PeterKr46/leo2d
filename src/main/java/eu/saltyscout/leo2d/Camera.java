package eu.saltyscout.leo2d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.render.RenderPhase;
import eu.saltyscout.leo2d.render.Renderer;
import eu.saltyscout.leo2d.util.data.PriorityQueue;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Camera {

    private final VoltImg volty;
    public double[] backgroundColor = new double[]{0.15, 0.15, 0.15};
    public double[] debugBackgroundColor = new double[]{0.15, 0.15, 0.15};
    private float verticalSize = 3;
    private Vector2 position = new Vector2(0, 0);
    // A list if layers which this camera renders. If empty, all.
    private List<Integer> layerMask = new ArrayList<>();

    public Camera() {
        // Create Volty Instance
        volty = new VoltImg();
        volty.bind(this);
    }


    public VoltImg getVolty() {
        return volty;
    }

    public Vector2 getPosition() {
        return position.copy();
    }

    public void setPosition(Vector2 position) {
        this.position = position.copy();
    }

    public Vector2 getHalfSize() {
        return new Vector2(verticalSize * Leo2D.getAspectRatio(), verticalSize);
    }

    public float getVerticalSize() {
        return verticalSize;
    }

    public void setVerticalSize(float verticalSize) {
        this.verticalSize = Math.max(0.3f, verticalSize);
    }

    public float getHorizontalSize() {
        return verticalSize * Leo2D.getAspectRatio();
    }

    public Vector2 getMin() {
        return getPosition().add(getHalfSize().multiply(-1));
    }

    public Vector2 getMax() {
        return getPosition().add(getHalfSize());
    }

    public boolean isLayerEnabled(int layer) {
        return layerMask.isEmpty() || layerMask.contains(layer);
    }

    public void setLayerEnabled(int layer, boolean enabled) {
        if (enabled) {
            if (!layerMask.contains(layer)) {
                layerMask.add(layer);
            }
        } else {
            layerMask.remove(layer);
        }
    }

    public Vector2 localize(Vector2 worldPos) {
        if (worldPos == null) {
            return null;
        }
        Vector2 diff = (worldPos.difference(position));
        diff.x /= (verticalSize * Leo2D.getAspectRatio());
        diff.y /= verticalSize;
        return diff;
    }

    public Vector2 localizePixelPos(Vector2 pixelPos) {
        if (pixelPos == null) {
            return null;
        }
        return new Vector2((pixelPos.x / Leo2D.getScreenWidth()), (pixelPos.y / Leo2D.getScreenHeight())).multiply(2).subtract(1, 1);
    }

    public Vector2 toWorldPos(Vector2 localPos) {
        Vector2 v = getPosition();
        v.add(localPos.x * getHorizontalSize(), localPos.y * getVerticalSize());
        return v;
    }

    private PriorityQueue<Renderer> buildRenderOrder(RenderPhase renderPhase) {
        // Create Render order
        PriorityQueue<Renderer> renderOrder = new PriorityQueue<>();
        List<Renderer> renderers = Scene.findAll(Renderer.class);
        AABB cullBounds = getAABB();
        for (Renderer renderer : renderers) {
            if (renderer.isEnabled() && renderer.getPhase() == renderPhase && isLayerEnabled(renderer.getLayer()) && (renderPhase == RenderPhase.GUI || cullBounds.overlaps(renderer.getAABB()))) {
                for (int passIndex : renderer.getPassIndices()) {
                    renderOrder.enqueue(renderer, passIndex);
                }
            }
        }
        return renderOrder;
    }

    void display(GLAutoDrawable drawable) {
        prepaint(drawable);
        for (RenderPhase phase : RenderPhase.values()) {
            // Render as ordered.
            PriorityQueue<Renderer> renderOrder = buildRenderOrder(phase);
            PriorityQueue<Renderer>.Holder re = renderOrder.dequeueHolder();
            while (re != null) {
                re.content.paint((int) re.priority);
                re = renderOrder.dequeueHolder();
            }
        }
    }


    /**
     * Prepares the Canvas before a frame is drawn.
     *
     * @param drawable
     */
    private void prepaint(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        if (Leo2D.isDebugEnabled()) {
            gl.glColor3d(debugBackgroundColor[0], debugBackgroundColor[1], debugBackgroundColor[2]);
        } else {
            gl.glColor3d(backgroundColor[0], backgroundColor[1], backgroundColor[2]);
        }
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(-1, -1);
        gl.glVertex2d(1, -1);
        gl.glVertex2d(1, 1);
        gl.glVertex2d(-1, 1);
        gl.glEnd();
        gl.glColor3d(1, 1, 1);
        if (!Leo2D.isDebugEnabled()) {
            return;
        }
        Vector2 min = getMin();
        Vector2 max = getMax();
        float color = 0.6f;
        for (int i = Math.toIntExact(Math.round(min.y)); i < max.y; i++) {
            float alpha = 0.2f;
            if (i % 10 == 0) {
                alpha = 0.6f;
            }
            volty.line(new Vector2(min.x, i), new Vector2(max.x, i), new double[]{color, color, color, alpha});
        }
        for (int i = Math.toIntExact(Math.round(min.x)); i < max.x; i++) {
            float alpha = 0.2f;
            if (i % 10 == 0) {
                alpha = 0.6f;
            }
            volty.line(new Vector2(i, min.y), new Vector2(i, max.y), new double[]{color, color, color, alpha});
        }

    }

    public AABB getAABB() {
        Vector2 min = getMin();
        Vector2 max = getMax();
        return new AABB(min, max);
    }

}
