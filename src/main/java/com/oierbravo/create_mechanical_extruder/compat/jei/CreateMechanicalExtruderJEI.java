package com.oierbravo.create_mechanical_extruder.compat.jei;

import com.oierbravo.create_mechanical_extruder.ModConstants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CreateMechanicalExtruderJEI implements IModPlugin {

    private static final ResourceLocation ID = ModConstants.asResource("jei_plugin");

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(ExtrudingCategory.INFO);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ExtrudingCategory.INFO.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ExtrudingCategory.INFO.registerCatalysts(registration);
    }

}
