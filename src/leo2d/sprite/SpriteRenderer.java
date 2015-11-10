package leo2d.sprite;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import leo2d.Transform;
import leo2d.core.Camera;
import leo2d.gl.VoltImg;
import leo2d.math.Rect;
import leo2d.math.Vector;

public class SpriteRenderer {
	private boolean enabled = true;
	private Transform transform;
	private int layer = 0, layerIndex = 0;
	public Sprite sprite;
	public SpriteRenderer(Transform transform) {
		this.transform = transform;	
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getLayer() {
		return layer;
	}

	public int getIndexInLayer() {
		return layerIndex;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void setIndexInLayer(int indexInLayer) {
		this.layerIndex = indexInLayer;
	}


	public void draw() {
		VoltImg volty = Camera.main().getVolty();

		if(sprite == null || sprite.getTexture() == null) {
			return;
		}
		Texture tex = sprite.getTexture();
		
		volty.enable(3553);
		tex.loadGLTexture(Camera.main().getGL());
		tex.getTexture(Camera.main().getGL()).bind(Camera.main().getGL());

		Rect sRect = sprite.getRect();
		double min_x = Math.min(1,sRect.getMinX() / tex.getWidth()),
			min_y = Math.min(1,sRect.getMinY() / tex.getHeight()),
			max_x = Math.min(1,sRect.getMaxX() / tex.getWidth()),
			max_y = Math.min(1,sRect.getMaxY() / tex.getHeight());
		
		Vector right = new Vector(transform.scale.x, 0).rotate(transform.rotation).multiply(sprite.getWidth() / sprite.getPPU());
		Vector up = new Vector(0, transform.scale.y).rotate(transform.rotation).multiply(sprite.getHeight() / sprite.getPPU());
		
		Vector bl = transform.position.clone().add(sprite.getOffset().multiply(transform.scale).rotate(transform.rotation)).toFixed();
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
		if(Camera.main().debug()) {
			double[] color = new double[] {0.5, 0.5, 0.5, 0.4};
			volty.line(tl,tr, color);
			volty.line(tl,bl, color);
			volty.line(bl,br, color);
			volty.line(br,tr, color);
		}
	}
}
