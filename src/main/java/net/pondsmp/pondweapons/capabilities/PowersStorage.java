package net.pondsmp.pondweapons.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTTypes;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.NBTTextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PowersStorage implements Capability.IStorage<IPowers> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPowers> capability, IPowers instance, Direction side) {
        ListNBT nbt = new ListNBT();
        for (String power : instance.getPowers()){
            nbt.add(StringNBT.valueOf(power));
        }
        return nbt;
    }

    @Override
    public void readNBT(Capability<IPowers> capability, IPowers instance, Direction side, INBT nbt) {
        if (!(instance instanceof Powers))
            throw new IllegalArgumentException("Only enter instances of Powers");
        for (INBT power : (ListNBT) nbt) {
            instance.addPower(power.getString());
            //might need to be ((StringNBT)power).getString()
        }
    }
}
