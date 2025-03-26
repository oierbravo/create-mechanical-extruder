package com.oierbravo.createmechanicalextruder.compat.jei;

import com.oierbravo.createmechanicalextruder.ModConstants;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

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
        CreateRecipeCategory.Factory<ExtrudingRecipe> factory = ExtrudingCategory::new;
        CreateRecipeCategory<ExtrudingRecipe> category = factory.create(ExtrudingCategory.INFO);

        registration.addRecipeCategories(category);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ExtrudingCategory.TYPE,ExtrudingCategory.INFO.recipes().get().stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ExtrudingCategory.INFO.catalysts().forEach(supplier -> registration.addRecipeCatalyst(supplier.get(),ExtrudingCategory.TYPE));
    }

}
