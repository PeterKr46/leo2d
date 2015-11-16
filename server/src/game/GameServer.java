package game;

import game.packet.EntityAliasPacket;
import game.packet.EntityIdPacket;
import game.packet.EntityMovePacket;
import game.packet.EntitySpawnPacket;
import net.client.UClient;
import net.packet.UPacket;
import net.server.UServer;

import java.io.IOException;

/**
 * Created by Peter on 25.05.2015.
 */
public class GameServer extends UServer {
    public GameServer(int port, int maxClients) throws IOException {
        super(port, maxClients);
    }

    public void broadcastAlias(UClient origin) {
        UPacket packet = new EntityAliasPacket(EntityManager.getInstance().getEntityId(origin.getUuid()), origin.getAlias());
        for(UClient client : clients) {
            if(client != origin) {
                client.sendPacket(packet);
            }
        }
    }

    @Override
    public void onConnect(UClient client) {
        EntityManager.Entity entity = EntityManager.getInstance().createEntity(client);
        int id = entity.getEntityId();
        log("Assigned Entity ID #" + id);
        UPacket spawnPacket = new EntitySpawnPacket(id, entity.getPosition());
        client.sendPacket(new EntityIdPacket(id));
        client.sendPacket(spawnPacket);
        for(UClient other : clients) {
            if(client != other) {
                log("Sent spawn packet to " + EntityManager.getInstance().getEntityId(other.getUuid()));
                EntityManager.Entity otherEnt = EntityManager.getInstance().getEntity(other.getUuid());
                other.sendPacket(spawnPacket);
                client.sendPacket(new EntitySpawnPacket(otherEnt.getEntityId()));
                client.sendPacket(new EntityMovePacket(otherEnt.getEntityId(), otherEnt.getPosition(), otherEnt.getDirection()));
                client.sendPacket(new EntityAliasPacket(otherEnt.getEntityId(), other.getAlias()));
            }
        }
    }

    @Override
    public void tick() {
        for(EntityManager.Entity moved : EntityManager.getInstance().wipeMoved()) {
            EntityMovePacket packet = new EntityMovePacket(moved.getEntityId(), moved.getPosition(), moved.getDirection());
            for(UClient other : clients) {
                if(other.getUuid() != moved.getUuid()) {
                    other.sendPacket(packet);
                }
            }
        }
    }

    public void broadcast(UPacket packet, UClient... exempt) {
        for(UClient other : clients) {
            boolean send = true;
            for(UClient ex : exempt) {
                if(other == ex) {
                    send = false;
                }
            }
            if(send) {
                other.sendPacket(packet);
            }
        }
    }
}
