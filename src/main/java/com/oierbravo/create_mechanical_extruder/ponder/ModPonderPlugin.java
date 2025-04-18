package com.oierbravo.create_mechanical_extruder.ponder;

import com.oierbravo.create_mechanical_extruder.ModConstants;
import com.oierbravo.create_mechanical_extruder.register.ModBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;

public class ModPonderPlugin implements PonderPlugin {
	@Override
	public @NotNull String getModId() {
		return ModConstants.MODID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		PonderSceneRegistrationHelper<ItemProviderEntry<?,?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

		HELPER.forComponents(ModBlocks.MECHANICAL_EXTRUDER)
				.addStoryBoard("mechanical_extruder_basic", ExtruderScenes::extruderBasic);
		HELPER.forComponents(ModBlocks.MECHANICAL_BRASS_EXTRUDER)
				.addStoryBoard("mechanical_extruder_basic", ExtruderScenes::extruderBasic);
	}

	@Override
	public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
		PonderTagRegistrationHelper<RegistryEntry<?,?>> TAG_HELPER = helper.withKeyFunction(RegistryEntry::getId);
		TAG_HELPER.addToTag(KINETIC_APPLIANCES)
				.add(ModBlocks.MECHANICAL_BRASS_EXTRUDER)
				.add(ModBlocks.MECHANICAL_EXTRUDER);
	}


}
