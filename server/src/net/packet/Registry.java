package net.packet;


import game.packet.*;

/**
 * Created by Peter on 25.05.2015.
 */
public class Registry {
    public static Class[] packets = new Class[] {
            DisconnectPacket.class,
            HeartbeatPacket.class,
            EntityMovePacket.class,
            EntityIdPacket.class,
            EntitySpawnPacket.class,
            EntityAliasPacket.class,
            ChatPacket.class
    };
}
