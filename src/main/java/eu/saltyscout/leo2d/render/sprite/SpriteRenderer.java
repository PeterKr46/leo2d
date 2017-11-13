package eu.saltyscout.leo2d.render.sprite;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import eu.saltyscout.leo2d.GameObject;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.render.RenderPhase;
import eu.saltyscout.leo2d.render.Renderer;
import eu.saltyscout.leo2d.sprite.Sprite;
import eu.saltyscout.leo2d.sprite.Texture;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;

public class SpriteRenderer implements Renderer {
    private final GameObject gameObject;
    private boolean enabled = true;

    private int layer = 0;
    private int[] passIndex = new int[]{0};
    private Sprite sprite = null;

    public SpriteRenderer(GameObject gameObject) {
        this.gameObject = gameObject;
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
    public GameObject getGameObject() {
        return gameObject;
    }

    @Override
    public void onDestroy() {

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
    public AABB getAABB() {
        if (sprite == null || sprite.getTexture() == null) {
            return new AABB(gameObject.getTransform().getTranslation(), gameObject.getTransform().getTranslation());
        }

        Vector2 scale = gameObject.getLocalScale();
        double rotation = gameObject.getTransform().getRotation();

        Vector2 right = new Vector2(scale.x, 0).rotate(rotation).multiply(sprite.getWidth() / sprite.getPPU());
        Vector2 up = new Vector2(0, scale.y).rotate(rotation).multiply(sprite.getHeight() / sprite.getPPU());
    // TODO
        Vector2 bl = new Vector2(gameObject.getTransform().getTranslation().add(sprite.getOffset().multiply(scale.x).rotate(rotation)));
        Vector2 br = bl.copy().add(right);
        Vector2 tr = bl.copy().add(right).add(up);
        Vector2 tl = bl.copy().add(up);
        double xMin = Math.min(bl.x, Math.min(br.x, Math.min(tr.x, tl.x)));
        double yMin = Math.min(bl.y, Math.min(br.y, Math.min(tr.y, tl.y)));
        double xMax = Math.max(bl.x, Math.max(br.x, Math.max(tr.x, tl.x)));
        double yMax = Math.max(bl.y, Math.max(br.y, Math.max(tr.y, tl.y)));
        return new AABB(xMin, yMin, xMax, yMax);
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

        AABB sRect = sprite.getRect();
        double min_x = Math.min(1f, sRect.getMinX() / tex.getWidth()),
                min_y = Math.min(1f, sRect.getMinY() / tex.getHeight()),
                max_x = Math.min(1f, sRect.getMaxX() / tex.getWidth()),
                max_y = Math.min(1f, sRect.getMaxY() / tex.getHeight());

        Vector2 scale = gameObject.getLocalScale();
        double rotation = gameObject.getTransform().getRotation();

        Vector2 right = new Vector2(scale.x, 0).rotate(rotation).multiply(sprite.getWidth() / sprite.getPPU());
        Vector2 up = new Vector2(0, scale.y).rotate(rotation).multiply(sprite.getHeight() / sprite.getPPU());
        // TODO
        Vector2 bl = new Vector2(gameObject.getTransform().getTranslation().add(sprite.getOffset().multiply(scale.x).rotate(rotation)));
        Vector2 br = bl.copy().add(right);
        Vector2 tr = bl.copy().add(right).add(up);
        Vector2 tl = bl.copy().add(up);

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
            double[] color = new double[]{0.5f, 0.5f, 0.5f, 0.4f};
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
