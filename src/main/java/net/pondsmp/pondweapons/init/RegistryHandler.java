package net.pondsmp.pondweapons.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.pondsmp.pondweapons.PondWeaponMod;
import net.pondsmp.pondweapons.tools.TestTier;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PondWeaponMod.MODID);
    public static final RegistryObject<SwordItem> Test_Ducky = ITEMS.register("testducky", () -> new SwordItem(TestTier.TestItem, 3, -2.8f, (new Item.Properties()).group(ItemGroup.COMBAT)));

}
