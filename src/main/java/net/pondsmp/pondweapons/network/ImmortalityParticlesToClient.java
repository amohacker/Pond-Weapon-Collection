package net.pondsmp.pondweapons.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.util.Date;

public class ImmortalityParticlesToClient {
    private boolean messageIsValid;
    private BlockPos pos;
    private Date date;
    public ImmortalityParticlesToClient(BlockPos pos, Date date){
        messageIsValid = true;
        this.pos = pos;
        this.date = date;
    }

    public ImmortalityParticlesToClient(){
        messageIsValid = false;
    }

    public boolean messageIsValid() {
        return messageIsValid;
    }

    public BlockPos getPos(){
        return this.pos;
    }

    public Date getDate() { return this.date;}

    public static ImmortalityParticlesToClient decode(PacketBuffer buf){
        ImmortalityParticlesToClient toClient = new ImmortalityParticlesToClient();
        toClient.pos = buf.readBlockPos();
        toClient.date = new Date(buf.readLong());
        toClient.messageIsValid = true;
        return toClient;
    }

    public void encode(PacketBuffer buf){
        if (!messageIsValid) return;
        buf.writeBlockPos(pos);
        buf.writeLong(date.getTime());
    }

    @Override
    public String toString() {
        return "ImmortalityParticlesToClient[pos=" + String.valueOf(pos) + ", date=" + date.toString() + "]";
    }
}