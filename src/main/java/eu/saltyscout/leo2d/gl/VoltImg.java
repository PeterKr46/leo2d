package eu.saltyscout.leo2d.gl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import eu.saltyscout.leo2d.Camera;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.math.Vector;

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

    public void texCoord(Vector tex) {
        texCoord(tex.getX(), tex.getY());
    }

    public void texCoord(float x, float y) {
        gl().glTexCoord2d(x, y);
    }

    public void vertex(Vector pos) {
        pos = camera.localize(pos);
        gl().glVertex2d(pos.getX(), pos.getY());
    }

    public void arrow(Vector start, Vector tip) {
        if(start == null || tip == null ||!isBound()) {
            return;
        }
        Vector notch = Vector.difference(tip, start).normalize().mul(camera.getVerticalSize() / 20).rotate(-25);
        line(start, tip);
        line(tip, tip.clone().add(notch));
        notch.rotate(50);
        line(tip, tip.clone().add(notch));
    }

    public void arrow(Vector start, Vector tip, float[] color) {
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

    public void line(Vector a, Vector b) {
        if (a == null || b == null) {
            return;
        }
        if (!isBound()) {
            return;
        }
        a = camera.localize(a);
        b = camera.localize(b);

        gl().glBegin(GL.GL_LINES);
        gl().glVertex2d(a.getX(), a.getY());
        gl().glVertex2d(b.getX(), b.getY());
        gl().glEnd();
    }

    public void line(Vector a, Vector b, float[] color) {
        if (!isBound()) {
            return;
        }

        if (color.length == 3) {
            gl().glColor3f(color[0], color[1], color[2]);
        } else if (color.length == 4) {
            gl().glColor4f(color[0], color[1], color[2], color[3]);
        }
        line(a, b);
        gl().glColor4d(1, 1, 1, 1);
    }

    public void circle(Vector center, float radius, float quality) {
        if (!isBound()) {
            return;
        }
        Vector delta = Vector.of(0, radius);
        float radDelta = 3.6f / quality;
        begin(GL.GL_LINE_LOOP);
        for (float rot = 0; rot < 360; rot += radDelta) {
            Vector d = delta.clone().rotate(rot);
            vertex(center.clone().add(d));
        }
        vertex(center.clone().add(delta));
        end();
    }

    public void circle(Vector center, float radius, float quality, float[] color) {
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

    public void filledCircle(Vector center, float radius, float quality) {
        if (!isBound()) {
            return;
        }
        Vector delta = Vector.of(0, radius);
        float radDelta = 3.6f / quality;
        begin(GL.GL_TRIANGLE_FAN);
        vertex(center);
        for (float rot = 0; rot < 360; rot += radDelta) {
            Vector d = delta.clone().rotate(rot);
            vertex(center.clone().add(d));
        }
        vertex(center.clone().add(delta));
        end();
    }

    public void filledCircle(Vector center, float radius, float quality, float[] color) {
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

    public void triangleFan(Vector center, Vector... vertices) {
        begin(GL.GL_TRIANGLE_FAN);
        vertex(center);
        for (Vector v : vertices) {
            vertex(v);
        }
        end();
    }

    public GL2 gl() {
        return Leo2D.getGL();
    }

    public void cross(Vector point) {
        begin(GL.GL_LINES);
        float d = camera.getVerticalSize() / 40;
        point = Vector.copyOnWrite(point);
        vertex(point.add(-d, d));
        vertex(point.add(d, -d));
        vertex(point.add(d, d));
        vertex(point.add(-d, -d));
        end();
    }
}
