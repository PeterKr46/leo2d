package eu.saltyscout.leo2d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.leo2d.math.Segment;
import eu.saltyscout.leo2d.render.RenderPhase;
import eu.saltyscout.leo2d.render.Renderer;
import eu.saltyscout.leo2d.util.data.PriorityQueue;
import eu.saltyscout.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class Camera {

    private final VoltImg volty;
    public double[] backgroundColor = new double[]{0.15, 0.15, 0.15};
    public double[] debugBackgroundColor = new double[]{0.15, 0.15, 0.15};
    private float verticalSize = 3;
    private Vector position = Vector.of(0, 0);
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

    public Vector getPosition() {
        return position.clone();
    }

    public void setPosition(Vector position) {
        this.position = position.clone();
    }

    public Vector getHalfSize() {
        return Vector.of(verticalSize * Leo2D.getAspectRatio(), verticalSize);
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

    public Vector getMin() {
        return getPosition().add(getHalfSize().mulComponents(-1, -1));
    }

    public Vector getMax() {
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

    public Vector localize(Vector worldPos) {
        if (worldPos == null) {
            return null;
        }
        Vector diff = Vector.difference(position.clone(), worldPos);
        diff.setX(diff.getX() / (verticalSize * Leo2D.getAspectRatio()));
        diff.setY(diff.getY() / verticalSize);
        return diff;
    }

    public Vector localizePixelPos(Vector pixelPos) {
        if (pixelPos == null) {
            return null;
        }
        return Vector.of((pixelPos.getX() / Leo2D.getScreenWidth()), (pixelPos.getY() / Leo2D.getScreenHeight())).mul(2).sub(1, 1);
    }

    public Vector toWorldPos(Vector localPos) {
        Vector v = getPosition();
        v.add(localPos.getX() * getHorizontalSize(), localPos.getY() * getVerticalSize());
        return v;
    }

    private PriorityQueue<Renderer> buildRenderOrder(RenderPhase renderPhase) {
        // Create Render order
        PriorityQueue<Renderer> renderOrder = new PriorityQueue<>();
        List<Renderer> renderers = Scene.findAll(Renderer.class);
        Rect cullBounds = getAABB();
        for (Renderer renderer : renderers) {
            if (renderer.isEnabled() && renderer.getPhase() == renderPhase && isLayerEnabled(renderer.getLayer()) && (renderPhase == RenderPhase.GUI || cullBounds.intersects(renderer.getAABB()))) {
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
        Vector min = getMin();
        Vector max = getMax();
        float color = 0.6f;
        for (int i = Math.round(min.getY()); i < max.getY(); i++) {
            float alpha = 0.2f;
            if (i % 10 == 0) {
                alpha = 0.6f;
            }
            volty.line(Vector.of(min.getX(), i), Vector.of(max.getX(), i), new float[]{color, color, color, alpha});
        }
        for (int i = Math.round(min.getX()); i < max.getX(); i++) {
            float alpha = 0.2f;
            if (i % 10 == 0) {
                alpha = 0.6f;
            }
            volty.line(Vector.of(i, min.getY()), Vector.of(i, max.getY()), new float[]{color, color, color, alpha});
        }

    }

    public Rect getAABB() {
        Vector min = getMin();
        Vector max = getMax();
        return new Rect(min, max);
    }

    public Segment[] getBoundingSegments() {
        Vector min = getMin();
        Vector max = getMax();
        return new Segment[]{
                new Segment(max, Vector.up().mul(-1), getVerticalSize() * 2),
                new Segment(Vector.of(min.getX(), max.getY()), Vector.right(),getHorizontalSize() * 2),
                new Segment(min, Vector.up(), getVerticalSize() * 2),
                new Segment(Vector.of(max.getX(), min.getY()), Vector.right().mul(-1),getHorizontalSize() * 2),
        };
    }
}
