package net.pondsmp.pondweapons.capabilities;

import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PowersCapability implements ICapabilitySerializable<ListNBT> {

    @CapabilityInject(IPowers.class)
    public static final Capability<IPowers> POWERS_CAPABILITY = null;
    private LazyOptional<IPowers> instance = LazyOptional.of(POWERS_CAPABILITY::getDefaultInstance);


    public static void register() {
        CapabilityManager.INSTANCE.register(IPowers.class, new PowersStorage(), Powers::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return POWERS_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public ListNBT serializeNBT() {
        return (ListNBT) POWERS_CAPABILITY.getStorage().writeNBT(POWERS_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")),null);
    }

    @Override
    public void deserializeNBT(ListNBT nbt) {
        POWERS_CAPABILITY.getStorage().readNBT(POWERS_CAPABILITY, instance.orElseThrow( () -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
