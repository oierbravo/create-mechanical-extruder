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
        //CreateRecipeCategory.Factory<ExtrudingRecipe> factory = ExtrudingCategory::new;
        //CreateRecipeCategory<ExtrudingRecipe> category = factory.create(ExtrudingCategory.INFO);

        registration.addRecipeCategories(ExtrudingCategory.INFO);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ExtrudingCategory.INFO.registerRecipes(registration);
        //registration.addRecipes(ExtrudingCategory.TYPE,ExtrudingCategory.INFO.recipes().get().stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ExtrudingCategory.INFO.registerCatalysts(registration);
        //ExtrudingCategory.INFO.catalysts().forEach(supplier -> registration.addRecipeCatalyst(supplier.get(),ExtrudingCategory.TYPE));
    }

}
