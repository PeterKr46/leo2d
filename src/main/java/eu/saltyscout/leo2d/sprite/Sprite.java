package eu.saltyscout.leo2d.sprite;

import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.math.Vector;

public class Sprite {
    private final Texture texture;
    private Vector offset = Vector.of(0, 0);
    private Rect rect;
    private float ppu;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.rect = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        this.ppu = texture.getHeight();
    }

    public Sprite(Texture texture, Rect rect) {
        this.texture = texture;
        this.rect = rect.clone();
        this.ppu = texture.getHeight();
    }

    public Vector getOffset() {
        return offset.clone();
    }

    public void setOffset(Vector offset) {
        this.offset = offset.clone();
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

    public Rect getRect() {
        return rect.clone();
    }

    public void setRect(Rect rect) {
        this.rect = rect.clone();
    }

    public float getHeight() {
        return rect.getHeight();
    }

    public float getWidth() {
        return rect.getWidth();
    }
}
