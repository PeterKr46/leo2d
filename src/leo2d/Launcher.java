package leo2d;

import leo2d.animation.Animation;
import leo2d.animation.Animator;
import leo2d.characters.CharacterCreator;
import leo2d.core.Camera;
import leo2d.core.Transform;
import leo2d.math.Vector;
import leo2d.sprite.Sprite;
import leo2d.sprite.SpriteSheet;
import leo2d.sprite.Texture;

public class Launcher {
	public static void main(String[] args) {
		Animation fireAnim = Animation.fromSpritesheet(new SpriteSheet(new Texture("assets/Fire.png")).slice(512,512));
		fireAnim.timePerFrame = 0.15;
		Sprite[] trees = new Sprite[] {
				new Sprite(new Texture("assets/Tree_1.png")),
				new Sprite(new Texture("assets/Tree_2.png")),
				new Sprite(new Texture("assets/Tree_3.png")),
		};
		Sprite[] bushes = new Sprite[] {
				new Sprite(new Texture("assets/Bush_1.png")),
				new Sprite(new Texture("assets/Bush_2.png")),
		};
		Sprite flowers = new Sprite(new Texture("assets/Flowers_1.png"));


		Camera camera = new Camera();
		camera.backgroundColor = new double[] {1,1,1};
		camera.debug = false;
		camera.setVerticalSize(6);

		for(int i = 0; i < 50; i++) {
			Transform nature = Transform.createEmpty("Nature");
			nature.position = new Vector((int) (Math.random() * 50) - 25,(int) (Math.random() * 50) - 25.5);
			nature.addRenderer().sprite = Math.random() > 0.9 ? flowers : Math.random() > 0.5 ? trees[((int) (Math.random() * (trees.length - 1)))] : bushes[((int) (Math.random() * (bushes.length - 1)))];
			if(nature.getRenderer().sprite == flowers) {
				nature.getRenderer().setLayer(1); // Always behind player
			} else {
				nature.getRenderer().setIndexInLayer((int) (nature.position.y * 100));
			}
			nature.getRenderer().sprite.setPPU(512);
		}

		Transform fire = Transform.createEmpty("Fire");
		fire.addRenderer();
		((Animator)fire.addComponent(Animator.class)).animation = fireAnim;

		CharacterCreator.createPlayer("91.41.81.9", 2000);

	}
}
