package eu.saltyscout.leo2d.gl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import eu.saltyscout.leo2d.Camera;
import eu.saltyscout.leo2d.Leo2D;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

public class VoltImg {
    private Camera camera;

    public VoltImg() {

    }

    public boolean isBound() {
        return camera != null && gl() != null;
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

    public void texCoord(Vector2 tex) {
        texCoord(tex.x, tex.y);
    }

    public void texCoord(double x, double y) {
        gl().glTexCoord2d(x, y);
    }

    public void vertex(Vector2 pos) {
        pos = camera.localize(pos);
        gl().glVertex2d(pos.x, pos.y);
    }

    public void arrow(Vector2 start, Vector2 tip) {
        if(start == null || tip == null ||!isBound()) {
            return;
        }
        Vector2 notch = start.difference(tip).getNormalized().multiply(camera.getVerticalSize() / 20).rotate(-0.5);
        line(start, tip);
        line(tip, tip.copy().add(notch));
        notch.rotate(1);
        line(tip, tip.copy().add(notch));
    }

    public void arrow(Vector2 start, Vector2 tip, float[] color) {
        if (!isBound()) {
            return;
        }
        if (color.length == 3) {
            gl().glColor3f(color[0], color[1], color[2]);
        } else if (color.length == 4) {
            gl().glColor4f(color[0], color[1], color[2], color[3]);
        }
        arrow(start, tip);
        gl().glColor4d(1, 1, 1, 1);
    }

    public void line(Vector2 a, Vector2 b) {
        if (a == null || b == null) {
            return;
        }
        if (!isBound()) {
            return;
        }
        a = camera.localize(a);
        b = camera.localize(b);

        gl().glBegin(GL.GL_LINES);
        gl().glVertex2d(a.x, a.y);
        gl().glVertex2d(b.x, b.y);
        gl().glEnd();
    }

    public void line(Vector2 a, Vector2 b, double[] color) {
        if (!isBound()) {
            return;
        }

        if (color.length == 3) {
            gl().glColor3d(color[0], color[1], color[2]);
        } else if (color.length == 4) {
            gl().glColor4d(color[0], color[1], color[2], color[3]);
        }
        line(a, b);
        gl().glColor4d(1, 1, 1, 1);
    }

    public void circle(Vector2 center, double radius, double quality) {
        if (!isBound()) {
            return;
        }
        Vector2 delta = new Vector2(0, radius);
        double radDelta = 2*Math.PI / quality;
        begin(GL.GL_LINE_LOOP);
        for (float i = 0; i < quality; i++) {
            delta.rotate(radDelta);
            vertex(center.copy().add(delta));
        }
        delta.rotate(radDelta);
        vertex(center.copy().add(delta));
        end();
    }

    public void circle(Vector2 center, double radius, double quality, double[] color) {
        if (!isBound()) {
            return;
        }
        if (color.length == 3) {
            gl().glColor3d(color[0], color[1], color[2]);
        } else if (color.length == 4) {
            gl().glColor4d(color[0], color[1], color[2], color[3]);
        }
        circle(center, radius, quality);
        gl().glColor4d(1, 1, 1, 1);
    }

    public void filledCircle(Vector2 center, float radius, float quality) {
        if (!isBound()) {
            return;
        }
        Vector2 delta = new Vector2(0, radius);
        double radDelta = 2*Math.PI / quality;
        begin(GL.GL_TRIANGLE_FAN);
        vertex(center);
        for (float i = 0; i < quality; i++) {
            delta.rotate(radDelta);
            vertex(center.copy().add(delta));
        }
        delta.rotate(radDelta);
        vertex(center.copy().add(delta));
        end();
    }

    public void filledCircle(Vector2 center, float radius, float quality, float[] color) {
        if (!isBound()) {
            return;
        }
        if (color.length == 3) {
            gl().glColor3d(color[0], color[1], color[2]);
        } else if (color.length == 4) {
            gl().glColor4d(color[0], color[1], color[2], color[3]);
        }
        filledCircle(center, radius, quality);
        gl().glColor4d(1, 1, 1, 1);
    }

    public void ellipse(Transform transform, double width, double height, double quality, double[] color) {
        if (!isBound()) {
            return;
        }
        if (color.length == 3) {
            gl().glColor3d(color[0], color[1], color[2]);
        } else if (color.length == 4) {
            gl().glColor4d(color[0], color[1], color[2], color[3]);
        }
        ellipse(transform, width, height, quality);
        gl().glColor4d(1, 1, 1, 1);
    }

    public void ellipse(Transform transform, double width, double height, double quality) {
        if (!isBound()) {
            return;
        }
        double radDelta = 2*Math.PI / quality;
        Vector2 tmp = new Vector2();
        Vector2 delta = new Vector2(0, 1);
        begin(GL.GL_LINE_LOOP);
        for (float i = 0; i < quality; i++) {
            delta.rotate(radDelta);
            tmp = delta.copy();
            tmp.x *= width;
            tmp.y *= height;
            vertex(transform.getTransformed(tmp));
        }
        delta.rotate(radDelta);
        vertex(transform.getTransformed(tmp));
        end();
    }

    public void triangleFan(Vector2 center, Vector2... vertices) {
        begin(GL.GL_TRIANGLE_FAN);
        vertex(center);
        for (Vector2 v : vertices) {
            vertex(v);
        }
        end();
    }

    public GL2 gl() {
        return Leo2D.getGL();
    }

    public void cross(Vector2 point) {
        begin(GL.GL_LINES);
        float d = camera.getVerticalSize() / 40;
        point = new Vector2(point);
        vertex(point.copy().add(-d, d));
        vertex(point.copy().add(d, -d));
        vertex(point.copy().add(d, d));
        vertex(point.copy().add(-d, -d));
        end();
    }
}
