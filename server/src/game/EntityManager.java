package game;


import game.packet.ChatPacket;
import net.client.UClient;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Peter on 25.05.2015.
 */
public class EntityManager {

    private static EntityManager instance;
    public static EntityManager getInstance() {
        if(instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    private HashMap<Integer, Entity> entities;
    private HashMap<UUID, Integer> entityIds;
    private CopyOnWriteArrayList<Entity> moved;
    private CopyOnWriteArrayList<Entity> turned;

    public EntityManager() {
        entities = new HashMap<>();
        entityIds = new HashMap<>();
        moved = new CopyOnWriteArrayList<>();
        turned = new CopyOnWriteArrayList<>();
    }

    public int getEntityId(UUID id) {
        return entityIds.containsKey(id) ? entityIds.get(id) : -1;
    }

    public Entity getEntity(int id) {
        return entityIds.containsValue(id) ? entities.get(id) : null;
    }

    public Entity getEntity(UUID id) {
        return entityIds.containsKey(id) ? entities.get(entityIds.get(id)) : null;
    }

    public Entity createEntity(UClient client) {
        int entityId = generateEntityId();
        UUID uuid = client == null ? UUID.randomUUID() : client.getUuid();
        float[] position = new float[] {0,0};
        Entity entity = new Entity(uuid, entityId, position, 0);
        entity.client = client;
        entities.put(entityId, entity);
        entityIds.put(uuid, entityId);
        return entity;
    }

    public int generateEntityId() {
        int id = 0;
        while(entityIds.containsValue(id)) {
            id++;
        }
        return id;
    }

    public boolean moved(UUID client, float[] currentPos) {
        float[] oldPos = entities.get(getEntityId(client)).position;
        return !(oldPos[0] == currentPos[0] && oldPos[1] == currentPos[1]);
    }

    public boolean turned(UUID client, float currentDirection) {
        float oldDirection = entities.get(getEntityId(client)).moveDirection;
        return oldDirection != currentDirection;
    }

    public Entity[] wipeMoved() {
        Entity[] res = moved.toArray(new Entity[moved.size()]);
        moved.clear();
        return res;
    }

    public Entity[] wipeTurned() {
        Entity[] res = turned.toArray(new Entity[turned.size()]);
        turned.clear();
        return res;
    }

    public static class Entity {

        private Entity(UUID uuid, int entityId, float[] position, int moveDirection) {
            this.uuid = uuid;
            this.entityId = entityId;
            this.position = position;
            this.moveDirection = moveDirection;
        }

        private UUID uuid;
        private int entityId;
        private float[] position;
        private int moveDirection;
        private int weapon;

        private UClient client;

        public UClient getClient() {
            return client;
        }

        public int getEntityId() {
            return entityId;
        }

        public void setPosition(float[] position) {
            this.position = position;
            EntityManager.instance.moved.add(this);
        }

        public void setDirection(int direction) {
            this.moveDirection = direction;
            EntityManager.instance.turned.add(this);
        }

        public UUID getUuid() {
            return uuid;
        }


        public void sendMessage(String message) {
            getClient().sendPacket(new ChatPacket(message));
        }

        public float[] getPosition() {
            return position;
        }

        public int getDirection() {
            return moveDirection;
        }

        public boolean isAt(float[] currentPos) {
            return (position[0] == currentPos[0] && position[1] == currentPos[1]);
        }

        public int getWeapon() {
            return weapon;
        }

        public void setWeapon(int weapon) {
            this.weapon = weapon;
        }
    }
}
