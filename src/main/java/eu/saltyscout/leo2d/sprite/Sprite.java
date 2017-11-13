package eu.saltyscout.leo2d.sprite;

import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;

public class Sprite {
    private final Texture texture;
    private Vector2 offset = new Vector2(0,0);
    private AABB rect;
    private float ppu;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.rect = new AABB(0, 0, texture.getWidth(), texture.getHeight());
        this.ppu = texture.getHeight();
    }

    public Sprite(Texture texture, AABB rect) {
        this.texture = texture;
        this.rect = new AABB(rect);
        this.ppu = texture.getHeight();
    }

    public Vector2 getOffset() {
        return offset.copy();
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset.copy();
    }

    public Texture getTexture() {
        return texture;
    }

    public float getPPU() {
        return ppu;
    }

    public void setPPU(float ppu) {
        this.ppu = (ppu <= 0 ? this.ppu : ppu);
    }

    public AABB getRect() {
        return new AABB(rect);
    }

    public void setRect(AABB rect) {
        this.rect = new AABB(rect);
    }

    public double getHeight() {
        return rect.getHeight();
    }

    public double getWidth() {
        return rect.getWidth();
    }
}
