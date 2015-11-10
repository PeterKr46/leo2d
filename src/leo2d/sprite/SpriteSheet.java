package leo2d.sprite;

import leo2d.Debug;
import leo2d.math.Rect;
import leo2d.math.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 10.11.2015.
 */
public class SpriteSheet {
    private List<Sprite> sprites = new ArrayList<>();
    private Texture texture;

    public Texture getTexture() {
        return texture;
    }

    public SpriteSheet(Texture texture) {
        this.texture = texture;
    }

    public Sprite[] getSprites() {
        return sprites.toArray(new Sprite[sprites.size()]);
    }

    public SpriteSheet slice(int width, int height) {
        sprites.clear();
        for(int x = 0; x + width <= texture.getWidth(); x += width) {
            for(int y = 0; y + height <= texture.getHeight(); y += height) {
                sprites.add(new Sprite(texture, new Rect(x,y, width, height)));
            }
        }
        Debug.log(texture.zDateiname + ": sliced into " + sprites.size() + " sprite(s).");
        return this;
    }

    public SpriteSheet offset(Vector offset) {
        for(Sprite sprite : sprites) {
            sprite.setOffset(offset);
        }
        return this;
    }
}
