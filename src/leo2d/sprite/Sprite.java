package leo2d.sprite;

import leo2d.math.Rect;
import leo2d.math.Vector;

public class Sprite {
	private Texture texture;
	private Vector offset = new Vector(0,0);
	private Rect rect;
	private double ppu;

	public Sprite(Texture texture) {
		this.texture = texture;
		this.rect = new Rect(0,0,texture.getWidth(), texture.getHeight());
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

	public double getPPU() {
		return ppu;
	}

	public void setPPU(double ppu) {
		this.ppu = (ppu <= 0 ? this.ppu : ppu);
	}
	
	public Rect getRect() {
		return rect.clone();
	}
	
	public void setRect(Rect rect) {
		this.rect = rect.clone();
	}

	public double getHeight() {
		return rect.getHeight();
	}

	public double getWidth() {
		return rect.getWidth();
	}
}
