package eu.saltyscout.leo2d.math;

import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.math.Vector;

/**
 * Created by peter on 7/18/15.
 */
public class Rect {
    private Vector min, max;

    public Rect(float xMin, float yMin, float width, float height) {
        min = Vector.of(xMin, yMin);
        max = Vector.of(xMin + width, yMin + height);
    }

    public Rect(Vector min, Vector max) {
        this.min = min.clone();
        this.max = max.clone();
    }

    public float getMinX() {
        return min.getX();
    }

    public void setMinX(float x) {
        min.setX(x);
    }

    public float getMinY() {
        return min.getY();
    }

    public void setMinY(float y) {
        min.setY(y);
    }

    public float getMaxX() {
        return max.getX();
    }

    public void setMaxX(float x) {
        max.setX(x);
    }

    public float getMaxY() {
        return max.getY();
    }

    public void setMaxY(float y) {
        max.setY(y);
    }

    public Vector getMin() {
        return min.clone();
    }

    public void setMin(Vector min) {
        this.min = min.clone();
    }

    public Vector getMax() {
        return max.clone();
    }

    public void setMax(Vector max) {
        this.max = max.clone();
    }

    public boolean intersects(Rect other) {
        return intersects(other, false);
    }

    private boolean intersects(Rect other, boolean oneDir) {
        return other != null && (contains(other.min) || contains(other.max) || contains(other.max.getX(), other.min.getY()) || contains(other.min.getX(), other.max.getY()) || (!oneDir && other.intersects(this, true)));
    }

    public boolean contains(float x, float y) {
        return x >= min.getX() && x <= max.getX() && y >= min.getY() && y <= max.getY();
    }

    public boolean contains(Vector v) {
        return v != null && v.getX() >= min.getX() && v.getX() <= max.getX() && v.getY() >= min.getY() && v.getY() <= max.getY();
    }

    @Override
    public Rect clone() {
        return new Rect(min.getX(), min.getY(), getWidth(), getHeight());
    }

    public float getWidth() {
        return max.getX() - min.getX();
    }

    public void setWidth(float width) {
        max.setX(min.getX() + width);
    }

    public float getHeight() {
        return max.getY() - min.getY();
    }

    public void setHeight(float height) {
        max.setY(min.getY() + height);
    }

    public void visualize() {
        float[] color = new float[]{0, 0.3f, 0.9f, 0.3f};
        VoltImg volty = Scene.getMainCamera().getVolty();
        volty.line(min, Vector.of(max.getX(), min.getY()), color);
        volty.line(min, Vector.of(min.getX(), max.getY()), color);
        volty.line(max, Vector.of(max.getX(), min.getY()), color);
        volty.line(max, Vector.of(min.getX(), max.getY()), color);
    }
}
