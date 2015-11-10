package net.packet;


import game.packet.*;

/**
 * Created by Peter on 25.05.2015.
 */
public class Registry {
    public static Class[] packets = new Class[] {
            DisconnectPacket.class,
            HeartbeatPacket.class,
            EntityPositionPacket.class,
            EntityIdPacket.class,
            EntitySpawnPacket.class,
            EntityDirectionPacket.class,
            EntityAliasPacket.class
    };
}
