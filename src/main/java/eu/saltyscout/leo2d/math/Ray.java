package eu.saltyscout.leo2d.math;

import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.core.Debug;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.util.data.Triplet;
import eu.saltyscout.math.Vector;

import java.awt.geom.Line2D;


/**
 * Created by Peter on 05.08.2015.
 */
public class Ray {
    public final float[] debugColor = new float[]{1, 0.3f, 0.3f};
    private Vector origin;
    private Vector direction;

    public Ray(Vector origin, Vector direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    public Vector getDirection() {
        return direction.clone();
    }

    public void setDirection(Vector direction) {
        this.direction = direction.clone().normalize();
        if (this.direction.sqrMagnitude() == 0) {
            this.direction = Vector.of(0, 1);
        }
    }

    public Vector getOrigin() {
        return origin.clone();
    }

    public void setOrigin(Vector origin) {
        this.origin = origin.clone();
    }

    public Vector eval(float scalar) {
        return getOrigin().add(getDirection().mul(scalar));
    }

    public double xOf(double y) {
        double s = (y - origin.getY()) / direction.getY();
        return origin.getX() + s * direction.getX();
    }

    public double yOf(double x) {
        double s = (x - origin.getX()) / direction.getX();
        return origin.getY() + s * direction.getY();
    }


    /**
     * Calculates the intersection of this Ray and another.
     *
     * @param other The second Ray.
     * @return A Triplet containing the intersection, the own scalar value of it and the scalar value of the other ray.
     */
    public Triplet<Vector, Float, Float> intersect(Ray other) {
        Vector direction = getDirection(), otherDirection = other.getDirection();
        Vector origin = getOrigin(), otherOrigin = other.getOrigin();
        if (Vector.isParallel(direction, otherDirection)) {
            // Parallels do not intersect.
            return new Triplet<>(null, 0.0f, 0.0f);
        }
        if(direction.getX() == 0) {
            // We're vertical!
            Vector hit = Vector.of(origin.getX(), other.yOf(direction.getX()));
            return new Triplet<>(hit, Vector.difference(origin, hit).magnitude(), Vector.difference(otherOrigin, hit).magnitude());
        } else if(otherDirection.getX() == 0) {
            // They're vertical!
            Vector hit = Vector.of(otherOrigin.getX(), yOf(direction.getX()));
            return new Triplet<>(hit, Vector.difference(origin, hit).magnitude(), Vector.difference(otherOrigin, hit).magnitude());
        }
        float r_dx = direction.getX(), r_dy = direction.getY(), r_px = origin.getX(), r_py = origin.getY();
        float s_dx = otherDirection.getX(), s_dy = otherDirection.getY(), s_px = otherOrigin.getX(), s_py = otherOrigin.getY();
        // Solve for T2!
        float T2 = (r_dx * (s_py - r_py) + r_dy * (r_px - s_px)) / (s_dx * r_dy - s_dy * r_dx);
        // Plug the value of T2 to get T1
        float T1 = (s_px + s_dx * T2 - r_px) / r_dx;
        // Debug.log(T1 + " " + T2);
        return new Triplet<>(eval(T1), T1, T2);
    }

    public void visualize() {
        float[] soft = new float[]{debugColor[0], debugColor[1], debugColor[2], 0.2f};
        Vector delta = getDirection();
        Vector endmarker = delta.clone().rotate(90).mul(Scene.getMainCamera().getVerticalSize() / 20);

        VoltImg volty = Scene.getMainCamera().getVolty();
        Vector origin = Vector.copyOnWrite(getOrigin());

        volty.line(origin, origin.add(delta), debugColor);
        volty.line(origin.sub(endmarker), origin.add(endmarker), soft);
        //volty.line(origin.add(delta).sub(endmarker), origin.add(delta).add(endmarker));
        /*volty.line(origin.add(delta).sub(endmarker.clone().rotate(-60)), origin.add(delta), debugColor);
		volty.line(origin.add(delta).add(endmarker.clone().rotate(60)), origin.add(delta), debugColor);*/
    }
}
