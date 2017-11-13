package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.GameObject;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.core.Debug;
import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.geometry.*;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by Peter on 07.11.2017.
 */
public class BoxCollider extends PolyCollider<Rectangle> {

    public BoxCollider(GameObject gameObject) {
        super(gameObject);
        setShape(new Rectangle(1, 1));
    }

    public void set(double width, double height) {
        setShape(new Rectangle(width, height));
    }

    public void setWidth(double width) {
        setShape(new Rectangle(width, getShape().getHeight()));
    }

    public void setHeight(double height) {
        setShape(new Rectangle(getShape().getWidth(), height));
    }


    @Override
    public RaycastResult cast(Ray ray) {
        throw new UnsupportedOperationException("Lazy fuck");
    }
}
