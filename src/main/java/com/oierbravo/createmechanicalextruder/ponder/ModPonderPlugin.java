package com.oierbravo.createmechanicalextruder.ponder;

import com.oierbravo.createmechanicalextruder.ModConstants;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModPonderPlugin implements PonderPlugin {

	@Override
	public @NotNull String getModId() {
		return ModConstants.MODID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		ModPonderScenes.register(helper);
	}

	@Override
	public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
		ModPonderTags.register(helper);
	}


}
