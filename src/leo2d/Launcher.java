package leo2d;

import leo2d.animation.Animation;
import leo2d.animation.Animator;
import leo2d.core.Camera;
import leo2d.math.Vector;
import leo2d.physics.IsometricController;
import leo2d.sprite.Sprite;
import leo2d.sprite.SpriteSheet;
import leo2d.sprite.Texture;

public class Launcher {
	public static void main(String[] args) {
		System.out.println("v0.01");
		Camera camera = new Camera();
		camera.backgroundColor = new double[] {0.3, 0.5, 1};
		camera.debug = true;
		camera.setVerticalSize(10);

		Texture iconTex = new Texture("assets/icon.png");
		Sprite iconSprite = new Sprite(iconTex);

		Texture targonTex = new Texture("assets/mt_targon.png");
		targonTex.enablePointFilter();
		Sprite targonSprite = new Sprite(targonTex);
		targonSprite.setPPU(targonSprite.getPPU()/3);

		SpriteSheet sheet = new SpriteSheet(new Texture("assets/test_sprite.png"));
		sheet.slice(8,8);
		Sprite[] frames = sheet.getSprites();

		Transform player = Transform.createEmpty("Light");
		player.position = new Vector(0.5, 5);
		player.addBehaviour(IsometricController.class);
		Animator animator = (Animator) player.addBehaviour(Animator.class);
		Animation animation = new Animation(4);
		for(int i = 0; i < 4; i++) {
			animation.getFrame(i).sprite = frames[i];
		}
		animator.animation = animation;
		player.addRenderer().sprite = iconSprite;
	}
}
