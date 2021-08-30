package net.pondsmp.pondweapons.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.pondsmp.pondweapons.PondWeaponMod;
import net.pondsmp.pondweapons.enchantments.Thunder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ObjectHolder(PondWeaponMod.MODID)
public class ModEnchantments
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final Enchantment thunder = new Thunder(Enchantment.Rarity.VERY_RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND);

    @Mod.EventBusSubscriber(modid = PondWeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler
    {

        /**
         * On event.
         *
         * @param event the event
         */
        @SubscribeEvent()
        public static void onEvent(final RegistryEvent.Register<Enchantment> event)
        {
            // DEBUG
            LOGGER.info("Registering Enchantments");
            System.out.println("Registering Enchantments");
            final IForgeRegistry<Enchantment> registry = event.getRegistry();

            registry.register(thunder);
        }
    }
}