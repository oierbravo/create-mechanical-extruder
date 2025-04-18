package com.oierbravo.create_mechanical_extruder.compat.kubejs.recipe;

import com.oierbravo.create_mechanical_extruder.ModConstants;
import com.oierbravo.create_mechanical_extruder.components.extruder.recipe.ExtrudingRecipe;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;

public class ExtrudingKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
            ModConstants.asResource(ExtrudingRecipe.Type.ID),
            ExtrudingKubeRecipe.class,
            ExtrudingKubeRecipe::new
    );
}
