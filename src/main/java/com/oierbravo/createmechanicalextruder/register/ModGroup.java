package com.oierbravo.createmechanicalextruder.register;


import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;


public class ModGroup extends CreativeModeTab {
	public static ModGroup MAIN;;
	
	public ModGroup(String name) {
		super(CreateMechanicalExtruder.MODID+":"+name);
		MAIN = this;
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModBlocks.MECHANICAL_EXTRUDER.get());
	}
}
