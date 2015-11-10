package game;

import game.packet.*;
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
        client.sendPacket(new EntityIdPacket(id));
        for(UClient other : clients) {
            if(client != other) {
                log("Sent spawn packet to " + EntityManager.getInstance().getEntityId(other.getUuid()));
                EntityManager.Entity otherEnt = EntityManager.getInstance().getEntity(other.getUuid());
                other.sendPacket(new EntitySpawnPacket(id));
                client.sendPacket(new EntitySpawnPacket(otherEnt.getEntityId()));
                client.sendPacket(new EntityPositionPacket(otherEnt.getEntityId(), otherEnt.getPosition(), otherEnt.isMoving()));
                client.sendPacket(new EntityDirectionPacket(otherEnt.getEntityId(), otherEnt.getFaceDirection(), otherEnt.getMoveDirection()));
                client.sendPacket(new EntityAliasPacket(otherEnt.getEntityId(), other.getAlias()));
            }
        }
    }

    @Override
    public void tick() {
        for(EntityManager.Entity moved : EntityManager.getInstance().wipeMoved()) {
            EntityPositionPacket packet = new EntityPositionPacket(moved.getEntityId(), moved.getPosition(), moved.isMoving());
            for(UClient other : clients) {
                if(other.getUuid() != moved.getUuid()) {
                    other.sendPacket(packet);
                }
            }
        }
        for(EntityManager.Entity turned : EntityManager.getInstance().wipeTurned()) {
            EntityDirectionPacket packet = new EntityDirectionPacket(turned.getEntityId(), turned.getFaceDirection(), turned.getMoveDirection());
            for(UClient other : clients) {
                if(other.getUuid() != turned.getUuid()) {
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
