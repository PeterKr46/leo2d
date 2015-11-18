package leo2d.characters;

import game.packet.ChatPacket;
import leo2d.animation.Animation;
import leo2d.animation.Animator;
import leo2d.client.Client;
import leo2d.client.ClientInThread;
import leo2d.client.ClientOutThread;
import leo2d.core.Transform;
import leo2d.math.Vector;
import leo2d.sprite.SpriteSheet;
import leo2d.sprite.Texture;

/**
 * Created by Peter on 10.11.2015.
 */
public class CharacterCreator {

    private static Animation horizontal = Animation.fromSpritesheet(new SpriteSheet(new Texture("assets/steve_horizontal.png")).slice(512, 512).offset(new Vector(-0.5,0)));
    private static Animation up = Animation.fromSpritesheet(new SpriteSheet(new Texture("assets/steve_up.png")).slice(512, 512).offset(new Vector(-0.5, 0)));
    private static Animation down = Animation.fromSpritesheet(new SpriteSheet(new Texture("assets/steve_down.png")).slice(512, 512).offset(new Vector(-0.5, 0)));
    private static Animation idle = Animation.fromSpritesheet(new SpriteSheet(new Texture("assets/steve_idle.png")).slice(512, 512).offset(new Vector(-0.5, 0)));

    public static Transform createNPC(String name, int entityId) {
        Transform transform = Transform.createEmpty(name);
        transform.addRenderer();
        transform.addComponent(Animator.class);
        NPC npc = (NPC) transform.addComponent(NPC.class);
        npc.horizontal = horizontal;
        npc.up = up;
        npc.down = down;
        npc.idle = idle;
        npc.setEntityId(entityId);

        return transform;
    }

    public static Transform createPlayer(String server, int port) {
        Transform transform = Transform.createEmpty("Player");
        transform.addRenderer();
        transform.addComponent(Animator.class);
        Player controller = (Player) transform.addComponent(Player.class);
        controller.horizontal = horizontal;
        controller.up = up;
        controller.down = down;
        controller.idle = idle;
        ClientInThread inThread = (ClientInThread) transform.addComponent(ClientInThread.class);
        ClientOutThread outThread = (ClientOutThread) transform.addComponent(ClientOutThread.class);
        inThread.client = new Client(server, port);
        outThread.client = inThread.client;
        outThread.outQueue.add(new ChatPacket("I start pley."));

        return transform;
    }
}
