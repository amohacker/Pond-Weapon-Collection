package net.pondsmp.pondweapons.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Currency;

public class CooldownStorage implements Capability.IStorage<ICooldown>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<ICooldown> capability, ICooldown instance, Direction side) {
        return IntNBT.valueOf (instance.getCooldown());
    }

    @Override
    public void readNBT(Capability<ICooldown> capability, ICooldown instance, Direction side, INBT nbt) {
        if (!(instance instanceof Cooldown))
            throw new IllegalArgumentException("Only enter instances of Cooldown");
        instance.setCooldown(((IntNBT)nbt).getInt());
    }
}
