package com.oierbravo.createmechanicalextruder.compat.kubejs;

import com.oierbravo.createmechanicalextruder.components.extruder.ExtrudingRecipe;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public class KubeJSCreateMechanicalExtruderPlugin extends KubeJSPlugin {


    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(ExtrudingRecipe.Serializer.ID, ExtrudingRecipeSchema.SCHEMA);
    }
}