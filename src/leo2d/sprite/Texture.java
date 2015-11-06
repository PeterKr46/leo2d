package leo2d.sprite;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.TextureIO;
import leo2d.core.Camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Texture
{
    private List<TexContainer> hTex = new ArrayList<TexContainer>();
    private int width = 0, height = 0;
    protected String zDateiname;

    // Only for init from texture, not path.
    private com.jogamp.opengl.util.texture.Texture directTex;

    public Texture(String pDateiname)
    {
        this.zDateiname = pDateiname;
        try {
            BufferedImage bimg = ImageIO.read(new File(zDateiname));
            width = bimg.getWidth();
            height = bimg.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texture(com.jogamp.opengl.util.texture.Texture lText) {
        this.directTex = lText;
        this.hTex.add(new TexContainer(lText, Camera.main().getGL()));
        this.width = lText.getWidth();
        this.height = lText.getHeight();
        this.zDateiname = null;
    }

    private boolean pointFilter = true;

    public void enablePointFilter() {
        for (TexContainer container : this.hTex) {
            com.jogamp.opengl.util.texture.Texture tex = container.hText;
            tex.setTexParameterf(container.kGL2, GL.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        }
        pointFilter = true;
    }

    public void enableAAFilter() {
        for (TexContainer aHTex : this.hTex) {
            com.jogamp.opengl.util.texture.Texture tex = aHTex.hText;
            tex.setTexParameterf(aHTex.kGL2, GL.GL_TEXTURE_MAG_FILTER, GL2.GL_INTERPOLATE);
        }
        pointFilter = false;
    }

    public void loadGLTexture(GL2 gl)
    {
        boolean lDa = false;
        for (TexContainer aHTex : this.hTex) {
            if (aHTex.kGL2 == gl) {
                lDa = true;
            }
        }
        if (!lDa && zDateiname != null) {
            com.jogamp.opengl.util.texture.Texture lText = null;
            try {
                lText = TextureIO.newTexture(new File(this.zDateiname), true);
                lText.setTexParameteri(gl, 10242, 10497);
                lText.setTexParameteri(gl, 10243, 10497);
                if(pointFilter) {
                    lText.setTexParameterf(gl, GL.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
                }
            } catch (Exception e) {
                System.out.println(e.toString() + ": " + this.zDateiname);
            }
            this.hTex.add(new TexContainer(lText, gl));
        } else if(zDateiname == null) {
            this.hTex.add(new TexContainer(directTex, gl));
        }
    }

    public com.jogamp.opengl.util.texture.Texture getTexture(GL2 pGL2) {
        com.jogamp.opengl.util.texture.Texture glTexture = null;
        for (TexContainer aHTex : this.hTex) {
            if (aHTex.kGL2 == pGL2) {
                glTexture = aHTex.hText;
            }
        }
        return glTexture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    class TexContainer
    {
        public com.jogamp.opengl.util.texture.Texture hText = null;
        public GL2 kGL2 = null;

        protected TexContainer(com.jogamp.opengl.util.texture.Texture pTex, GL2 pGL2) {
            this.hText = pTex; this.kGL2 = pGL2;
        }
    }
}
