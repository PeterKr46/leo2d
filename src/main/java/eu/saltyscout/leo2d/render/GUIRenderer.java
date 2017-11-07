package eu.saltyscout.leo2d.render;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.leo2d.render.sprite.SpriteRenderer;
import eu.saltyscout.leo2d.sprite.BorderedSprite;
import eu.saltyscout.leo2d.sprite.Sprite;
import eu.saltyscout.leo2d.sprite.Texture;
import eu.saltyscout.math.Vector;

/**
 * Created by Peter on 03.11.2017.
 */
public class GUIRenderer extends SpriteRenderer {

    public GUIRenderer(Transform transform) {
        super(transform);
    }

    @Override
    public final RenderPhase getPhase() {
        return RenderPhase.GUI;
    }

    /**
     * GUI Renderers do not need an AABB check, so we will not offer one.
     * @return null.
     */
    @Override
    public final Rect getAABB() {
        return null;
    }

    @Override
    public void paint(int pass) {
        VoltImg volty = Scene.getMainCamera().getVolty();
        Sprite sprite = getSprite();
        if (sprite == null || sprite.getTexture() == null) {
            return;
        }
        if(sprite instanceof BorderedSprite) {
            float borderStep = ((BorderedSprite) sprite).getBorderWidth();
            Transform transform = getTransform();
            Texture tex = sprite.getTexture();

            volty.enable(3553);
            tex.loadGLTexture(Leo2D.getGL());
            tex.getTexture(volty.gl()).bind(volty.gl());

            Rect sRect = sprite.getRect();
            float min_x = Math.min(1f, sRect.getMinX() / tex.getWidth()),
                    min_y = Math.min(1f, sRect.getMinY() / tex.getHeight()),
                    max_x = Math.min(1f, sRect.getMaxX() / tex.getWidth()),
                    max_y = Math.min(1f, sRect.getMaxY() / tex.getHeight());
            float tcWidth = max_x - min_x;
            float tcHeight = max_y - min_y;

            Vector scale = transform.getLocalScale();
            float rotation = transform.getRotation();

            Vector right = Vector.copyOnWrite(Vector.of(scale.getX(), 0).rotate(rotation).mul(sprite.getWidth() / sprite.getPPU()));
            Vector up = Vector.copyOnWrite(Vector.of(0, scale.getY()).rotate(rotation).mul(sprite.getHeight() / sprite.getPPU()));

            Vector bl = Vector.copyOnWrite(transform.getPosition().add(sprite.getOffset().mulComponents(scale).rotate(rotation)));
            Vector tl = Vector.copyOnWrite(bl.add(up));

            Vector offsetRightSmall = right.normalize().mul(borderStep);
            Vector offsetRightLarge = right.sub(offsetRightSmall);
            Vector offsetDownSmall = up.normalize().mulComponents(1, -borderStep);
            Vector offsetDownMiddle = up.mulComponents(1,-1).sub(offsetDownSmall.clone().mulComponents(1,2));

            /*
            0----1---------2----3
            |    |         |    |
            4----5---------6----7
            |    |         |    |
            |    |         |    |
            8----9---------10---11
            |    |         |    |
            12---13--------14---15
             */
            Vector[] verts = new Vector[16];
            Vector[] texCoords = new Vector[16];
            for(int i = 0; i < 4; i++) {
                verts[i*4]      = tl.clone();
                texCoords[i*4]  = Vector.of(min_x, max_y);
                verts[i*4+1]    = tl.add(offsetRightSmall);
                texCoords[i*4+1]= Vector.of(min_x + tcWidth * borderStep, max_y);
                verts[i*4+2]    = tl.add(offsetRightLarge);
                texCoords[i*4+2]= Vector.of(max_x - tcWidth * (borderStep), max_y);
                verts[i*4+3]    = tl.add(right);
                texCoords[i*4+3]= Vector.of(max_x, max_y);
                tl = Vector.copyOnWrite(tl.add(i == 1 ? offsetDownMiddle : offsetDownSmall));
                max_y -= (i == 1 ? (1-borderStep*2) : borderStep) * tcHeight;
            }

            int[] tris = new int[]{
                    0,1,5,  0,4,5,
                    1,2,6,  1,5,6,
                    2,3,7,  2,6,7,
                    4,5,9,  4,8,9,
                    5,6,10, 5,9,10,
                    6,7,11, 6,10,11,
                    8,9,13, 8,12,13,
                    9,10,14,9,13,14,
                    10,11,15,10,14,15
            };

            volty.enable(GL.GL_BLEND);
            volty.blendFunc(GL2.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            volty.begin(GL2.GL_TRIANGLES);
            for (int v : tris) {
                volty.texCoord(texCoords[v]);
                volty.vertex(verts[v]);
            }

            volty.end();
            volty.disable(3553);
        } else {
            super.paint(pass);
        }
    }
}
