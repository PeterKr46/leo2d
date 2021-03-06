package eu.saltyscout.leo2d.sprite;

import org.dyn4j.geometry.AABB;

/**
 * Created by Peter on 06.11.2017.
 */
public class BorderedSprite extends Sprite {
    private float borderWidth = 0.125f;
    public BorderedSprite(Texture texture) {
        super(texture);
    }

    public BorderedSprite(Texture texture, AABB rect) {
        super(texture, rect);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }
}
