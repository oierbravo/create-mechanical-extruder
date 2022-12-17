package com.oierbravo.createmechanicalextruder.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.compat.jei.animations.AnimatedExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtrudingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.resources.ResourceLocation;

public class ExtrudingCategory extends CreateRecipeCategory<ExtrudingRecipe> {
    private AnimatedExtruder extruder = new AnimatedExtruder();
    public final static ResourceLocation UID = new ResourceLocation(CreateMechanicalExtruder.MODID, "extruding");
    public ExtrudingCategory(Info<ExtrudingRecipe> info) {
        super(info);
    }


    public void setRecipe(IRecipeLayoutBuilder builder, ExtrudingRecipe recipe, IFocusGroup focuses) {
        int slotIndex = 0;
        int initX = 11;
        int initY = 30;
        int distance = 43;
        for(int i= 0;i <recipe.getItemIngredients().size();i++ ){
            builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getItemIngredients().get(i));
            slotIndex++;
        }
        for(int i= 0;i <recipe.getFluidIngredients().size();i++ ){
            builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addIngredients(ForgeTypes.FLUID_STACK,recipe.getFluidIngredients().get(i).getMatchingFluidStacks());
            slotIndex++;
        }
        if(!recipe.getCatalyst().isEmpty())
            builder.addSlot(RecipeIngredientRole.INPUT,  34,57)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStack(recipe.getCatalyst());

        ProcessingOutput output = recipe.getResult();
        builder.addSlot(RecipeIngredientRole.OUTPUT,  130,29)
                .setBackground(getRenderedSlot(), -1, -1)
                .addTooltipCallback(addStochasticTooltip(output))
                .addItemStack(recipe.getResultItem());

    }


    public void draw(ExtrudingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(matrixStack, 80, 32); //Output arrow
        extruder.draw(matrixStack, 53, 55);
    }

    public void drawBonks(ExtrudingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack) {

    }
}
