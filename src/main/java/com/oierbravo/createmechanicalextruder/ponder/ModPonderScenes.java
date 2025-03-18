package com.oierbravo.createmechanicalextruder.ponder;

import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?,?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(ModBlocks.MECHANICAL_EXTRUDER)
                .addStoryBoard("mechanical_extruder_basic", ExtruderScenes::extruderBasic);
        HELPER.forComponents(ModBlocks.MECHANICAL_BRASS_EXTRUDER)
                .addStoryBoard("mechanical_extruder_basic", ExtruderScenes::extruderBasic);

    }
}
