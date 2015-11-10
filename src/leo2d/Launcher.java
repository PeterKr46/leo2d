package leo2d;

import leo2d.core.Camera;
import leo2d.math.Vector;
import leo2d.physics.IsometricController;
import leo2d.physics.collider.BoxCollider;
import leo2d.physics.collider.CircleCollider;
import leo2d.sprite.Sprite;
import leo2d.sprite.Texture;

public class Launcher {
	public static void main(String[] args) {
		System.out.println("v0.01");
		Camera camera = new Camera();
		camera.debug = true;
		camera.setVerticalSize(10);

		Texture iconTex = new Texture("assets/icon.png");
		Sprite iconSprite = new Sprite(iconTex);

		Texture targonTex = new Texture("assets/mt_targon.png");
		targonTex.enablePointFilter();
		Sprite targonSprite = new Sprite(targonTex);
		targonSprite.setPPU(targonSprite.getPPU()/3);

		Transform mtTargon = Transform.createEmpty("Mt. Targon");
		mtTargon.rotation = 20;
		mtTargon.addRenderer().sprite = targonSprite;
		BoxCollider coll = (BoxCollider) mtTargon.addBehaviour(BoxCollider.class);
		coll.setSize(3, 0.5);

		Transform light = Transform.createEmpty("Light");
		light.position = new Vector(0.5, 5);
		light.addBehaviour(IsometricController.class);
		light.addRenderer().sprite = iconSprite;

		Transform circle = Transform.createEmpty("Circle");
		circle.position = new Vector(2,2);
		circle.addBehaviour(CircleCollider.class);
	}
}
