package net.pondsmp.pondweapons.ItemGroup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.pondsmp.pondweapons.init.ModItems;

public class ModItemGroup {

    public static final ItemGroup POWERS = new ItemGroup("powers") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.IMMORTALITALS_HEART);
        }
    };
}
