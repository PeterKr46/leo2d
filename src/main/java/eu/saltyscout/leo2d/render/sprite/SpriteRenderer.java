package eu.saltyscout.leo2d.render.sprite;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.leo2d.render.RenderPhase;
import eu.saltyscout.leo2d.render.Renderer;
import eu.saltyscout.leo2d.sprite.Sprite;
import eu.saltyscout.leo2d.sprite.Texture;
import eu.saltyscout.math.Vector;

public class SpriteRenderer implements Renderer {
    private final Transform transform;
    private boolean enabled = true;

    private int layer = 0;
    private int[] passIndex = new int[]{0};
    private Sprite sprite = null;

    public SpriteRenderer(Transform transform) {
        this.transform = transform;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void earlyUpdate() {
        // Nothing.
    }

    @Override
    public void update() {
        // Nothing.
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int[] getPassIndices() {
        return passIndex;
    }

    public void setIndexInLayer(int indexInLayer) {
        this.passIndex[0] = indexInLayer;
    }

    @Override
    public RenderPhase getPhase() {
        return RenderPhase.DEFAULT;
    }

    @Override
    public Rect getAABB() {
        if (sprite == null || sprite.getTexture() == null) {
            return new Rect(transform.getPosition(), transform.getPosition());
        }

        Vector scale = transform.getLocalScale();
        float rotation = transform.getRotation();

        Vector right = Vector.of(scale.getX(), 0).rotate(rotation).mul(sprite.getWidth() / sprite.getPPU());
        Vector up = Vector.of(0, scale.getY()).rotate(rotation).mul(sprite.getHeight() / sprite.getPPU());

        Vector bl = Vector.copyOnWrite(transform.getPosition().add(sprite.getOffset().mulComponents(scale).rotate(rotation)));
        Vector br = bl.add(right);
        Vector tr = bl.add(right).add(up);
        Vector tl = bl.add(up);
        float xMin = Math.min(bl.getX(), Math.min(br.getX(), Math.min(tr.getX(), tl.getX())));
        float yMin = Math.min(bl.getY(), Math.min(br.getY(), Math.min(tr.getY(), tl.getY())));
        float xMax = Math.max(bl.getX(), Math.max(br.getX(), Math.max(tr.getX(), tl.getX())));
        float yMax = Math.max(bl.getY(), Math.max(br.getY(), Math.max(tr.getY(), tl.getY())));
        return new Rect(xMin, yMin, xMax - xMin, yMax - yMin);
    }

    @Override
    public void paint(int pass) {
        VoltImg volty = Scene.getMainCamera().getVolty();
        if (sprite == null || sprite.getTexture() == null) {
            return;
        }
        Texture tex = sprite.getTexture();

        volty.enable(3553);
        tex.loadGLTexture(Leo2D.getGL());
        tex.getTexture(volty.gl()).bind(volty.gl());

        Rect sRect = sprite.getRect();
        float min_x = Math.min(1f, sRect.getMinX() / tex.getWidth()),
                min_y = Math.min(1f, sRect.getMinY() / tex.getHeight()),
                max_x = Math.min(1f, sRect.getMaxX() / tex.getWidth()),
                max_y = Math.min(1f, sRect.getMaxY() / tex.getHeight());

        Vector scale = transform.getLocalScale();
        float rotation = transform.getRotation();

        Vector right = Vector.of(scale.getX(), 0).rotate(rotation).mul(sprite.getWidth() / sprite.getPPU());
        Vector up = Vector.of(0, scale.getY()).rotate(rotation).mul(sprite.getHeight() / sprite.getPPU());

        Vector bl = Vector.copyOnWrite(transform.getPosition().add(sprite.getOffset().mulComponents(scale).rotate(rotation)));
        Vector br = bl.add(right);
        Vector tr = bl.add(right).add(up);
        Vector tl = bl.add(up);

        volty.enable(GL.GL_BLEND);
        volty.blendFunc(GL2.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        volty.begin(GL2.GL_QUADS);

        volty.texCoord(min_x, max_y);
        volty.vertex(tl);

        volty.texCoord(min_x, min_y);
        volty.vertex(bl);

        volty.texCoord(max_x, min_y);
        volty.vertex(br);

        volty.texCoord(max_x, max_y);
        volty.vertex(tr);

        volty.end();
        volty.disable(3553);
        if (Leo2D.isDebugEnabled()) {
            float[] color = new float[]{0.5f, 0.5f, 0.5f, 0.4f};
            volty.line(tl, tr, color);
            volty.line(tl, bl, color);
            volty.line(bl, br, color);
            volty.line(br, tr, color);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
