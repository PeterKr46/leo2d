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

import java.io.File;

public class Launcher {
	public static void main(String[] args) {
		Animation fireAnim = Animation.fromSpritesheet(new SpriteSheet(new Texture("assets/Fire.png")).slice(512,512));
		fireAnim.timePerFrame = 0.15;
		File terrainAssetsFolder = new File("assets/Terrain/");
		File[] terrainAssets = terrainAssetsFolder.listFiles();
		Sprite[] terrainSprites = new Sprite[terrainAssets.length];
		for(int i = 0; i < terrainAssets.length; i++) {
			terrainSprites[i] = new Sprite(new Texture(terrainAssets[i].getPath()));
		}


		Camera camera = new Camera();
		camera.backgroundColor = new double[] {1,1,1};
		camera.debug = false;
		camera.setVerticalSize(6);

		for(int tex = 0; tex < terrainSprites.length; tex++) {
			int id = 0;
			while(id < 10) {
				Transform nature = Transform.createEmpty(tex + "T" + id);
				nature.position = new Vector((int) (Math.random() * 50) - 25,(int) (Math.random() * 50) - 25.5);
				nature.addRenderer().sprite = terrainSprites[tex];
				nature.tag = "Terrain";
				if(tex == -1) {
					nature.getRenderer().setLayer(1); // Always behind player
				} else {
					nature.getRenderer().setIndexInLayer((int) (nature.position.y * 100));
				}
				nature.getRenderer().sprite.setPPU(512);
				id++;
			}
		}

		Transform fire = Transform.createEmpty("Fire");
		fire.addRenderer();
		((Animator)fire.addComponent(Animator.class)).animation = fireAnim;

		CharacterCreator.createPlayer("87.168.82.18", 2000);

	}
}
