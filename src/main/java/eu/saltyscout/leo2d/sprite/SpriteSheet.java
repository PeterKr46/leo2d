package eu.saltyscout.leo2d.sprite;

import eu.saltyscout.leo2d.core.Debug;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 10.11.2015.
 */
public class SpriteSheet {
    private List<Sprite> sprites = new ArrayList<>();
    private Texture texture;

    public SpriteSheet(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite[] getSprites() {
        return sprites.toArray(new Sprite[sprites.size()]);
    }

    public SpriteSheet slice(int width, int height) {
        sprites.clear();
        for (int y = 0; y + height <= texture.getHeight(); y += height) {
            for (int x = 0; x + width <= texture.getWidth(); x += width) {
                sprites.add(new Sprite(texture, new AABB(x, y, x+width, y+height)));
            }
        }
        Debug.log(texture.zDateiname + ": sliced into " + sprites.size() + " sprite(s).");
        return this;
    }

    public SpriteSheet offset(Vector2 offset) {
        for (Sprite sprite : sprites) {
            sprite.setOffset(offset);
        }
        return this;
    }
}
