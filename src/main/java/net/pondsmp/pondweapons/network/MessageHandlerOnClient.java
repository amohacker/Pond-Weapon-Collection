package net.pondsmp.pondweapons.network;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.pondsmp.pondweapons.PondWeaponMod;
import net.pondsmp.pondweapons.powers.Immortality;

import java.awt.*;
import java.util.Optional;
import java.util.function.Supplier;

public class MessageHandlerOnClient {
    public static <MSG> void onMessageReceived(ImmortalityRespawnToClient msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);
        if (sideReceived != LogicalSide.CLIENT) {
            PondWeaponMod.LOGGER.warn("ImmortalityRespawnToClient received on wrong side: " + ctx.getDirection().getReceptionSide());
            return;
        }
        if (!msg.messageIsValid()) {
            PondWeaponMod.LOGGER.warn("ImmortalityRespawnToClient was invalid " + msg.toString());
        }

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
        if (!clientWorld.isPresent()) {
            PondWeaponMod.LOGGER.warn("ImmortalityRespawnToClient context could not provide a ClientWorld.");
            return;
        }
        ctx.enqueueWork(() -> processMessage(clientWorld.get(), msg));
    }

    private static void processMessage(ClientWorld worldClient, ImmortalityRespawnToClient message) {
        if (message.getRespawn()) {
            Immortality.playerdied();
        }
    }


    public static <MSG> void onMessageReceived(ImmortalityParticlesToClient msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);
        if (sideReceived != LogicalSide.CLIENT) {
            PondWeaponMod.LOGGER.warn("ImmortalityRespawnToClient received on wrong side: " + ctx.getDirection().getReceptionSide());
            return;
        }
        if (!msg.messageIsValid()) {
            PondWeaponMod.LOGGER.warn("ImmortalityRespawnToClient was invalid " + msg.toString());
        }

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
        if (!clientWorld.isPresent()) {
            PondWeaponMod.LOGGER.warn("ImmortalityRespawnToClient context could not provide a ClientWorld.");
            return;
        }
        ctx.enqueueWork(() -> processMessage(clientWorld.get(), msg));
    }

    private static void processMessage(ClientWorld clientWorld, ImmortalityParticlesToClient msg) {
        final int RADIUS = 10;
        final BasicParticleType type = ParticleTypes.PORTAL;
        Immortality.drawSphere(clientWorld, msg.getPos(), RADIUS, type);
    }
}
