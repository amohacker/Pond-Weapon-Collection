package net.pondsmp.pondweapons.capabilities;

import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CooldownCapability implements ICapabilitySerializable<IntNBT> {

    @CapabilityInject(ICooldown.class)
    public static final Capability<ICooldown> COOLDOWN_CAPABILITY = null;
    private LazyOptional<ICooldown> instance = LazyOptional.of(COOLDOWN_CAPABILITY::getDefaultInstance);

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ICooldown.class, new CooldownStorage(), Cooldown::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return COOLDOWN_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public IntNBT serializeNBT() {
    return (IntNBT) COOLDOWN_CAPABILITY.getStorage().writeNBT(COOLDOWN_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(IntNBT nbt) {
        COOLDOWN_CAPABILITY.getStorage().readNBT(COOLDOWN_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
