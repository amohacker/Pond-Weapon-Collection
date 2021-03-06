package net.pondsmp.pondweapons.init;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.pondsmp.pondweapons.ItemGroup.ModItemGroup;
import net.pondsmp.pondweapons.PondWeaponMod;
import net.pondsmp.pondweapons.powers.ForbiddenFruit;
import net.pondsmp.pondweapons.powers.ImmortalsHeart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ObjectHolder(PondWeaponMod.MODID)
public class ModItems
{
    private static final Logger LOGGER = LogManager.getLogger();


    public static final Item IMMORTALITALS_HEART = new ImmortalsHeart(new Item.Properties().group(ModItemGroup.POWERS).food((new Food.Builder()).hunger(0).saturation(10.0F).setAlwaysEdible().build())).setRegistryName("immortals_heart");
    public static final Item FORBIDDEN_FRUIT = new ForbiddenFruit(new Item.Properties().group(ModItemGroup.POWERS).food((new Food.Builder()).hunger(0).saturation(10.0F).setAlwaysEdible().build())).setRegistryName("forbidden_fruit");

    @Mod.EventBusSubscriber(modid = PondWeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler
    {

        /**
         * On event.
         *
         * @param event the event
         */
        @SubscribeEvent()
        public static void onEvent(final RegistryEvent.Register<Item> event)
        {
            // DEBUG
            LOGGER.info("Registering Items");
            final IForgeRegistry<Item> registry = event.getRegistry();

            registry.register(IMMORTALITALS_HEART);
            registry.register(FORBIDDEN_FRUIT);
        }
    }
}