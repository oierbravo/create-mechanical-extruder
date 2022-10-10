package com.oierbravo.createmechanicalextruder.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeTypesEvent;
import net.minecraft.resources.ResourceLocation;

public class KubeJSCreateMechanicalExtruderPlugin extends KubeJSPlugin {


    //@Override
    public void registerRecipeTypes(RegisterRecipeTypesEvent event) {
        event.register(new ResourceLocation("create_mechanical_extruder:extruding"), ExtrudingRecipeJS::new);
    }
}