package net.pondsmp.pondweapons.enchantments;

import com.google.gson.internal.$Gson$Preconditions;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.IntNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.OnlyIns;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.FMLWorldPersistenceHook;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.pondsmp.pondweapons.PondWeaponMod;
import net.pondsmp.pondweapons.capabilities.CooldownCapability;
import net.pondsmp.pondweapons.capabilities.ICooldown;
import net.pondsmp.pondweapons.init.ModEnchantments;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.jmx.Server;
import org.lwjgl.system.CallbackI;


import javax.annotation.Nonnull;
import java.io.DataOutput;
import java.io.IOException;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.Map;

public class Thunder extends Enchantment {
//    private final Enchantment.Rarity rarity= Rarity.VERY_RARE;
//    private final EnchantmentType type = EnchantmentType.WEAPON;
//    private final EquipmentSlotType slots = EquipmentSlotType.MAINHAND;

    private final int minlvl = 1;
    private final int maxlvl = 5;
    private final boolean treasureEnch = true;
    private final boolean curse = false;
    private final boolean tradable = false;
    private final boolean loot = false;
    private final boolean book = false;
    private static CooldownTracker cooldownTracker = new CooldownTracker();
    protected final static int cooldownStart = 60;

    public Thunder(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
        setRegistryName("thunder");
    }

    public Thunder(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType slots) {
        super(rarityIn, typeIn, new EquipmentSlotType[]{slots});
        setRegistryName("thunder");
    }

    /**
     * Returns the minimum level that the enchantment can have.
     */
    @Override
    public int getMinLevel() {
        return this.minlvl;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    @Override
    public int getMaxLevel() {
        return maxlvl;
    }


    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
     */
    @Override
    public int calcModifierDamage(int level, DamageSource source) {
        return 0;
    }

    /**
     * Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
     * calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
     */
    @Override
    public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        return 0.0F;
    }

    /**
     * Called whenever a mob is damaged with an item that has this enchantment on it.
     */
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
    }

    /**
     * Whenever an entity that has this enchantment on one of its associated items is damaged this method will be called.
     */
    public void onUserHurt(LivingEntity user, Entity attacker, int level) {
    }

    @Override
    public boolean isTreasureEnchantment() {
        return treasureEnch;
    }

    @Override
    public boolean isCurse() {
        return curse;
    }

    /**
     * Checks if the enchantment can be sold by villagers in their trades.
     */
    @Override
    public boolean canVillagerTrade() {
        return tradable;
    }

    /**
     * Checks if the enchantment can be applied to loot table drops.
     */
    @Override
    public boolean canGenerateInLoot() {
        return loot;
    }

    /**
     * This applies specifically to applying at the enchanting table. The other method {@link #canApply(ItemStack)}
     * applies for <i>all possible</i> enchantments.
     * @param stack
     * @return
     */

    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.canApplyAtEnchantingTable(this);
    }

    /**
     * Is this enchantment allowed to be enchanted on books via Enchantment Table
     * @return false to disable the vanilla feature
     */

    public boolean isAllowedOnBooks() {
        return book;
    }

    @Mod.EventBusSubscriber(modid = PondWeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Events{


        @SubscribeEvent
        public static void rightClickEvent(PlayerInteractEvent.RightClickItem event) throws Throwable {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(event.getPlayer().getHeldItemMainhand());
            if (!event.getWorld().isRemote && enchantments.get(ModEnchantments.thunder) != null) {
                    ICooldown cooldown = event.getPlayer().getCapability(CooldownCapability.COOLDOWN_CAPABILITY).orElseThrow(new NonNullSupplier<Throwable>() {
                        @Nonnull
                        @Override
                        public Throwable get() {
                            return null;
                        }
                    });
                if (!cooldown.hasCooldown())
                {
                    int level = enchantments.get(ModEnchantments.thunder);
                    int thisCooldown;
                    if (level < 12) {
                        thisCooldown = (cooldownStart-(5*level))*20;
                    } else {
                        thisCooldown = (int) (5*20/ Math.pow((double) 2, (double) level-11));
                    }
                    cooldown.setCooldown(thisCooldown);
                    ArrayList<String> list = new ArrayList<String>();
                    RayTraceResult block = event.getPlayer().pick(250, 0, true);
                    if(block.getType() == RayTraceResult.Type.BLOCK) {
                        BlockPos blockpos = ((BlockRayTraceResult) block).getPos();
                        LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(event.getWorld());
                        lightning.setLocationAndAngles((double) blockpos.getX(), (double) blockpos.getY()+1, (double) blockpos.getZ(), (float) 0.0, (float) 0.0);
                        event.getPlayer().getHeldItemMainhand().getMaxDamage();
                        event.getWorld().addEntity(lightning);
                    }
                }
                else {
                    event.getPlayer().sendStatusMessage(new StringTextComponent("Cooldown: " + cooldown.getCooldown()/20), true);
                }
            }
        }

        @SubscribeEvent
        public static void tickEvent(TickEvent.PlayerTickEvent event){
            if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
                event.player.getCapability(CooldownCapability.COOLDOWN_CAPABILITY).ifPresent(new NonNullConsumer<ICooldown>() {
                    @Override
                    public void accept(@Nonnull ICooldown iCooldown) {
                        if (iCooldown.tick()) {
                            event.player.sendStatusMessage(new StringTextComponent("Cooldown has ended"), true);
                        }
                    }
                });
            }
        }

/*        @SubscribeEvent
        public static void cooldownTickEvent(PlayerEvent event) {
        }*/

        @SubscribeEvent
        public static void attachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof PlayerEntity) {
                event.addCapability(new ResourceLocation(PondWeaponMod.MODID, "cooldown"), new CooldownCapability());
            }
        }
    }
}
