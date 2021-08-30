package net.pondsmp.pondweapons.network;

import net.minecraft.network.PacketBuffer;

public class ImmortalityRespawnToClient {
    private boolean messageIsValid;
    private boolean respawn;
    public ImmortalityRespawnToClient(boolean respawn){
        messageIsValid = true;
        this.respawn = true;
    }

    public ImmortalityRespawnToClient(){
        messageIsValid = false;
    }

    public boolean messageIsValid() {
        return messageIsValid;
    }

    public boolean getRespawn(){
        return this.respawn;
    }

    public static ImmortalityRespawnToClient decode(PacketBuffer buf){
        ImmortalityRespawnToClient toClient = new ImmortalityRespawnToClient();
        toClient.respawn = buf.readBoolean();
        toClient.messageIsValid = true;
        return toClient;
    }

    public void encode(PacketBuffer buf){
        if (!messageIsValid) return;
        buf.writeBoolean(respawn);
    }

    @Override
    public String toString() {
        return "ImmortalityRespawnToClient[respawn=" + String.valueOf(respawn) + "]";
    }
}