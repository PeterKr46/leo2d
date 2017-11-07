package eu.saltyscout.leo2d.physics;

import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.core.Debug;
import eu.saltyscout.leo2d.math.Ray;
import eu.saltyscout.leo2d.physics.collider.CircleCollider;
import eu.saltyscout.math.Vector;

/**
 * Created by Peter on 06.11.2017.
 */
public class Rigidbody implements Component {

    private final Transform transform;
    private boolean enabled = true;
    private float mass = 1f;
    private float elasticity = 0.1f;

    private Vector velocity = Vector.zero();

    public Rigidbody(Transform transform) {
        this.transform = transform;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void earlyUpdate() {

    }

    @Override
    public void update() {
        velocity.add(Vector.up().mul(-1 * Leo2D.deltaTime()));
        CircleCollider collider = getTransform().getComponent(CircleCollider.class);
        Ray ray = new Ray(collider.getCenter(), velocity.clone().normalize().mul(collider.getRadius() + 0.01f));
        ray.visualize();
        Physics.RaycastHit hit;
        if((hit = Physics.cast(ray, collider)).point != null) {
            //Debug.log(hit.hitDistance);
            if(hit.hitDistance <= collider.getRadius() + velocity.magnitude() * Leo2D.deltaTime() * 2 + 0.001f) {
                Rigidbody otherBody = hit.collider.getTransform().getComponent(Rigidbody.class);
                Vector normal = hit.normal.clone();
                Vector dir = velocity.clone().normalize();
                float t = Vector.dotProd(dir, normal);
                Vector mirrored = dir.sub(normal.mul(2 * t)).normalize();
                velocity = mirrored.mul(velocity.magnitude()).add(hit.normal.clone().mul(0.001f));
                new Ray(hit.point, hit.normal).visualize();
                // Leo2D.pause();
                Debug.log("Bounced against " + hit.collider.getTransform().name + " towards " + mirrored);
            }
        }
        transform.setPosition(transform.getPosition().add(velocity.clone().mul(Leo2D.deltaTime())));
    }

    @Override
    public Transform getTransform() {
        return transform;
    }
}
