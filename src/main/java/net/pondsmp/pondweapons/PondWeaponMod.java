package net.pondsmp.pondweapons;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.pondsmp.pondweapons.capabilities.CooldownCapability;
import net.pondsmp.pondweapons.capabilities.PowersCapability;
import net.pondsmp.pondweapons.init.RegistryHandler;
import net.pondsmp.pondweapons.network.ImmortalityParticlesToClient;
import net.pondsmp.pondweapons.network.ImmortalityRespawnToClient;
import net.pondsmp.pondweapons.network.MessageHandlerOnClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pondweaponcollection")
public class PondWeaponMod
{
    public static final String MODID = "pondweaponcollection";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static SimpleChannel simpleChannel;
    private static final String PROTOCOL_VERSION = "1.0";
    public static final byte IMMORTALITY_MESSAGE_ID = 1;
    public static final byte IMMORTALITY_RESPAWN_EFFECT = 2;

    public PondWeaponMod() {

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        CooldownCapability.register();
        PowersCapability.register();
        simpleChannel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(PondWeaponMod.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);

        simpleChannel.registerMessage(IMMORTALITY_MESSAGE_ID, ImmortalityRespawnToClient.class,
                ImmortalityRespawnToClient::encode, ImmortalityRespawnToClient::decode,
                MessageHandlerOnClient::onMessageReceived,
                Optional.of(PLAY_TO_CLIENT));

        simpleChannel.registerMessage(IMMORTALITY_RESPAWN_EFFECT, ImmortalityParticlesToClient.class,
                ImmortalityParticlesToClient::encode, ImmortalityParticlesToClient::decode,
                MessageHandlerOnClient::onMessageReceived,
                Optional.of(PLAY_TO_CLIENT));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
    }
}
