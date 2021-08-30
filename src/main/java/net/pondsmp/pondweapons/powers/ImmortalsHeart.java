package net.pondsmp.pondweapons.powers;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.util.NonNullSupplier;
import net.pondsmp.pondweapons.capabilities.IPowers;
import net.pondsmp.pondweapons.capabilities.PowersCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class ImmortalsHeart extends Item {

    public ImmortalsHeart(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            try {
                IPowers powers = entityLiving.getEntity().getCapability(PowersCapability.POWERS_CAPABILITY).orElseThrow(() -> {
                    return new Exception("POWERS_CAPABILITY not found on entity" + entityLiving.getEntityString());
                });
                if (!powers.hasPower("immortality")) {
                    powers.addPower("immortality");
                    entityLiving.getEntity().sendMessage(new StringTextComponent("The alphas smile upon you"), null);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
