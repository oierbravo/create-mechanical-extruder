package com.oierbravo.createmechanicalextruder.compat.jei;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.compat.jei.animations.AnimatedExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import com.oierbravo.createmechanicalextruder.foundation.recipe.requirements.BiomeRequirement;
import com.oierbravo.createmechanicalextruder.foundation.recipe.requirements.MaxHeightRequirement;
import com.oierbravo.createmechanicalextruder.foundation.recipe.requirements.MinHeightRequirement;
import com.oierbravo.createmechanicalextruder.foundation.recipe.requirements.SpeedRequirement;
import com.oierbravo.createmechanicalextruder.foundation.utility.ModLang;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ExtrudingCategory extends CreateRecipeCategory<ExtrudingRecipe> {
    private AnimatedExtruder extruder = new AnimatedExtruder();
    public final static ResourceLocation UID = new ResourceLocation(CreateMechanicalExtruder.MODID, "extruding");
    public ExtrudingCategory(Info<ExtrudingRecipe> info) {
        super(info);
    }


    public void setRecipe(IRecipeLayoutBuilder builder, ExtrudingRecipe recipe, IFocusGroup focuses) {
        int slotIndex = 0;
        int initX = 12;
        int initY = 30;
        int distance = 42;
        for(int i= 0;i <recipe.getItemIngredients().size();i++ ){
            builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getItemIngredients().get(i));
            slotIndex++;
        }
        for(int i= 0;i <recipe.getFluidIngredients().size();i++ ){
            builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addIngredients(ForgeTypes.FLUID_STACK,recipe.getFluidIngredients().get(i).getMatchingFluidStacks());
            slotIndex++;
        }
        if(!recipe.getCatalyst().isEmpty())
            builder.addSlot(RecipeIngredientRole.INPUT,  33,57)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStack(recipe.getCatalyst());

        ProcessingOutput output = recipe.getResult();
        builder.addSlot(RecipeIngredientRole.OUTPUT,  130,29)
                .setBackground(getRenderedSlot(), -1, -1)
                .addTooltipCallback(addStochasticTooltip(output))
                .addItemStack(recipe.getResultItem());

    }


    public void draw(ExtrudingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 80, 32); //Output arrow
        extruder.draw(graphics, 53, 55);
        drawBonks(recipe, graphics, 55,55);
        drawBiome(recipe, graphics, 55,65);
        drawMinHeight(recipe, graphics, 55,13);
        drawMaxHeight(recipe, graphics, 55,3);
        drawMinSpeed(recipe, graphics, 100,55);

    }
    protected void drawBonks(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        guiGraphics.drawString(fontRenderer,  ModLang.translate("goggles.bonks",recipe.getRequiredBonks()).string(), x, y, 0xFF808080, false);
    }
    protected void drawBiome(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            BiomeRequirement biomeRequirement = recipe.getRequirement(BiomeRequirement.TYPE);
            if(biomeRequirement != null) {
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;
                guiGraphics.drawString(fontRenderer, biomeRequirement.toString(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }
    protected void drawMinHeight(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            MinHeightRequirement minHeightRequirement = (MinHeightRequirement) recipe.getRequirement(MinHeightRequirement.TYPE);
            if(minHeightRequirement != null) {
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;
                guiGraphics.drawString(fontRenderer, ModLang.translate("ui.recipe_requirement.min_height", minHeightRequirement.toString()).string(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }
    protected void drawMaxHeight(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            MaxHeightRequirement maxHeightRequirement = (MaxHeightRequirement) recipe.getRequirement(MaxHeightRequirement.TYPE);
            if(maxHeightRequirement != null) {
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;
                guiGraphics.drawString(fontRenderer, ModLang.translate("ui.recipe_requirement.max_height", maxHeightRequirement.toString()).string(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }
    protected void drawMinSpeed(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            SpeedRequirement speedRequirement = (SpeedRequirement) recipe.getRequirement(SpeedRequirement.TYPE);
            if(speedRequirement != null) {
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;
                guiGraphics.drawString(fontRenderer, ModLang.translate("ui.recipe_requirement.min_speed", speedRequirement.toString()).string(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }
}
