package leo2d.sprite.font;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import leo2d.core.Camera;
import leo2d.core.Debug;
import leo2d.gl.VoltImg;
import leo2d.math.Rect;
import leo2d.math.Vector;
import leo2d.sprite.Sprite;
import leo2d.sprite.SpriteSheet;
import leo2d.sprite.Texture;

import java.io.File;
import java.util.HashMap;

/**
 * Created by peter on 7/20/15.
 */
public class Font extends SpriteSheet {
    private Font(Texture texture, String name) {
        super(texture);
    }

    private void setChar(char c, Sprite sprite) {
        characterSpriteMap.put(c, sprite);
    }
    private HashMap<Character, Sprite> characterSpriteMap = new HashMap<>();

    public static Font load(String path) {
        return load(path, 16, 32);
    }

    public static Font load(String path, int charWidth, int charHeight) {
        File image = new File(path);
        if(!image.exists() || !image.isFile()) {
            Debug.log("No image at '" + path + "'!");
            return null;
        }
        String fontName = image.getName().replace(".png", "").replace(".jpg", "").replace(".jpeg", "");
        Texture texture = new Texture(path);
        //texture.enableAAFilter();
        Font font = new Font(texture, fontName);
        char[] layout = new char[] {
                '0','1','2','3','4','5','6','7','8','9',' ',' ',' ',
                'ä','ö','ü','ß',',','.',':','!','?','(',')','<','>',
                'n','o','p','q','r','s','t','u','v','w','x','y','z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m',
        };
        font.slice(charWidth, charHeight);
        Sprite[] sprites = font.getSprites();
        for(int i = 0; i < sprites.length; i++) {
            font.setChar(layout[i], sprites[i]);
            sprites[i].setPPU(32);
        }
        return font;
    }

    public void write(Vector position, String text, double size, float rotation) {
        write(position, text, size, rotation, new float[]{0,0,0});
    }

    public void write(Vector position, String text, double size, float rotation, float[] rgb) {
        Camera camera = Camera.main();
        VoltImg volty = camera.getVolty();
        text = text.toLowerCase();
        for(int i = 0; i < text.length(); i++) {
            char chr = text.charAt(i);
            Sprite sprite = characterSpriteMap.get(chr);
            if(sprite != null) {
                Vector scaledOffset = new Vector(i*size*0.5,0).rotate(rotation);
                Texture tex = sprite.getTexture();

                volty.enable(3553);
                tex.loadGLTexture(Camera.main().getGL());
                tex.getTexture(Camera.main().getGL()).bind(Camera.main().getGL());

                Rect sRect = sprite.getRect();
                double min_x = Math.min(1,sRect.getMinX() / tex.getWidth()),
                        min_y = Math.min(1,sRect.getMinY() / tex.getHeight()),
                        max_x = Math.min(1,sRect.getMaxX() / tex.getWidth()),
                        max_y = Math.min(1,sRect.getMaxY() / tex.getHeight());

                Vector right = new Vector(size, 0).rotate(rotation).multiply(sprite.getWidth() / sprite.getPPU());
                Vector up = new Vector(0, size).rotate(rotation).multiply(sprite.getHeight() / sprite.getPPU());

                Vector bl = position.clone().add(scaledOffset).clone().add(sprite.getOffset().rotate(rotation)).toFixed();
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
            }

        }

    }

}
