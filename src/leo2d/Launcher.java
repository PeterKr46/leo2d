package leo2d;

import leo2d.core.Camera;
import leo2d.math.Vector;
import leo2d.physics.CharacterController;
import leo2d.physics.collider.BoxCollider;
import leo2d.physics.collider.CircleCollider;
import leo2d.sprite.Sprite;
import leo2d.sprite.Texture;

public class Launcher {
	public static void main(String[] args) {
		System.out.println("v0.01");
		Camera camera = new Camera();
		camera.debug = true;
		
		Texture targonTex = new Texture("assets/mt_targon.png");
		targonTex.enablePointFilter();
		Sprite targonSprite = new Sprite(targonTex);
		targonSprite.setPPU(targonSprite.getPPU()/3);
		Transform mtTargon = Transform.createEmpty("Mt. Targon");
		mtTargon.rotation = 20;
		BoxCollider coll = (BoxCollider) mtTargon.addBehaviour(BoxCollider.class);
		coll.setSize(3, 0.5);
		//coll.edges = new Ray[] {new Ray(new Vector(-0.5, 0.5), new Vector(2, 0.1))};
		Transform light = Transform.createEmpty("Light");
		light.position = new Vector(0.5, 5);
		light.addBehaviour(CharacterController.class);
		//light.addBehaviour(Rotate.class);
		Transform circle = Transform.createEmpty("Circle");
		circle.position = new Vector(5,5);
		circle.addBehaviour(CircleCollider.class);
		mtTargon.addRenderer().sprite = targonSprite;
	}
}
