package net.pondsmp.pondweapons.powers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.pondsmp.pondweapons.capabilities.IPowers;
import net.pondsmp.pondweapons.capabilities.PowersCapability;

public class ImmortalityBluepill extends Item {
    public ImmortalityBluepill(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            try {
                IPowers powers = entityLiving.getCapability(PowersCapability.POWERS_CAPABILITY).orElseThrow(() -> {
                    return new Exception("POWERS_CAPABILITY not found on entity " + entityLiving.getEntityString());
                });
                if (powers.hasPower("immortality")) {
                    powers.removePower("immortality");
                    entityLiving.sendMessage(new StringTextComponent("The alphas look down on you"), null);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
