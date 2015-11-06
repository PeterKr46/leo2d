package leo2d.gl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import leo2d.core.Camera;
import leo2d.math.Vector;

public class VoltImg {
	private Camera camera;
	
	public boolean isBound() {
		return camera != null && gl() != null;
	}

	public VoltImg() {
		
	}

	public boolean bind(Camera camera) {

		this.camera = camera;
		return isBound();
	}
	
	public void enable(int id) {
		gl().glEnable(id);
	}
	
	public void disable(int id) {
		gl().glDisable(id);
	}
	
	public void blendFunc(int a, int b) {
		gl().glBlendFunc(a, b);
	}
	
	public void begin(int id) {
		gl().glBegin(id);
	}
	
	public void end() {
		gl().glEnd();
	}
	
	public void texCoord(Vector tex) {
		texCoord(tex.x, tex.y);
	}
	
	public void texCoord(double x, double y) {
		gl().glTexCoord2d(x, y);
	}
	
	public void vertex(Vector pos) {
		pos = camera.localize(pos);
		gl().glVertex2d(pos.x, pos.y);
	}
	
	public void line(Vector a, Vector b) {
		if(a == null || b == null) {
			return;
		}
		if(!isBound()) {
			return;		
		}
		a = camera.localize(a);
		b = camera.localize(b);
		
		gl().glBegin(GL.GL_LINES);
		gl().glVertex2d(a.x, a.y);
		gl().glVertex2d(b.x, b.y);
		gl().glEnd();
	}
	
	public void line(Vector a, Vector b, double[] color) {
		if(!isBound()) {
			return;		
		}
		
		if(color.length == 3) {
			gl().glColor3d(color[0], color[1], color[2]);
		} else if(color.length == 4) {
			gl().glColor4d(color[0], color[1], color[2], color[3]);
		}
		line(a, b);
		gl().glColor4d(1, 1, 1, 1);
	}

	public void circle(Vector center, double radius, double quality) {
		if(!isBound()) {
			return;
		}
		Vector delta = new Vector(0, radius);
		double radDelta = 3.6/quality;
		begin(GL.GL_LINE_LOOP);
		for(double rot = 0; rot < 360; rot += radDelta) {
			Vector d = delta.clone().rotate(rot);
			vertex(center.clone().add(d));
		}
		vertex(center.clone().add(delta));
		end();
	}

	public void circle(Vector center, double radius, double quality, double[] color) {
		if(!isBound()) {
			return;
		}
		if(color.length == 3) {
			gl().glColor3d(color[0], color[1], color[2]);
		} else if(color.length == 4) {
			gl().glColor4d(color[0], color[1], color[2], color[3]);
		}
		circle(center, radius, quality);
		gl().glColor4d(1,1,1,1);
	}
	
	public void filledCircle(Vector center, double radius, double quality) {
		if(!isBound()) {
			return;
		}
		Vector delta = new Vector(0, radius);
		double radDelta = 3.6/quality;
		begin(GL.GL_TRIANGLE_FAN);
		vertex(center);
		for(double rot = 0; rot < 360; rot += radDelta) {
			Vector d = delta.clone().rotate(rot);
			vertex(center.clone().add(d));
		}
		vertex(center.clone().add(delta));
		end();
	}

	public void filledCircle(Vector center, double radius, double quality, double[] color) {
		if(!isBound()) {
			return;
		}
		if(color.length == 3) {
			gl().glColor3d(color[0], color[1], color[2]);
		} else if(color.length == 4) {
			gl().glColor4d(color[0], color[1], color[2], color[3]);
		}
		filledCircle(center, radius, quality);
		gl().glColor4d(1,1,1,1);
	}
	
	public void triangleFan(Vector center, Vector... vertices) {
		begin(GL.GL_TRIANGLE_FAN);
		vertex(center);
		for(Vector v : vertices) {
			vertex(v);
		}
		end();
	}

	private GL2 gl() {
		return camera.getGL();
	}

	public void cross(Vector point) {
		begin(GL.GL_LINES);
		double d = Camera.main().getVerticalSize()/40;
		point = point.toFixed();
		vertex(point.add(-d, d));
		vertex(point.add(d, -d));
		vertex(point.add(d, d));
		vertex(point.add(-d, -d));
		end();
	}
}
