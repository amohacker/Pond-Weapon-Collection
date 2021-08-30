package net.pondsmp.pondweapons.powers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.pondsmp.pondweapons.PondWeaponMod;
import net.pondsmp.pondweapons.capabilities.CooldownCapability;
import net.pondsmp.pondweapons.capabilities.PowersCapability;

import java.util.ArrayList;

public class PowersController {
    @Mod.EventBusSubscriber(modid = PondWeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Events {

        @SubscribeEvent
        public static void attachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) {
                event.addCapability(new ResourceLocation(PondWeaponMod.MODID, "powers"), new PowersCapability());
            }
        }

        @SubscribeEvent
        public static void playerCloneEvent(PlayerEvent.Clone event){
            if (!event.isWasDeath()) {
                return;
            }
            ArrayList<String> originalPowers = event.getOriginal()
                    .getCapability(PowersCapability.POWERS_CAPABILITY)
                    .orElseThrow(() -> new NullPointerException(
                            "POWERS_CAPABILITY could not be found on player "+ event.getOriginal().toString())
                    ).getPowers();

            event.getPlayer().getCapability(PowersCapability.POWERS_CAPABILITY)
                    .orElseThrow(() -> new NullPointerException(
                            "POWERS_CAPABILITY could not be found on player "+ event.getPlayer().toString())
                    ).setPowers(originalPowers);

        }
    }
}
