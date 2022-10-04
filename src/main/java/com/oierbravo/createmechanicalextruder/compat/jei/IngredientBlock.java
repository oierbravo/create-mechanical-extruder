package com.oierbravo.createmechanicalextruder.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.CustomLightingSettings;
import com.simibubi.create.foundation.gui.ILightingSettings;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class IngredientBlock implements IDrawable {
    Block block;
    public IngredientBlock(Block block){
        this.block = block;
    }
    public IngredientBlock(FluidStack fluidStack){
        this.block = block;
    }
    public static final ILightingSettings DEFAULT_LIGHTING = CustomLightingSettings.builder()
            .firstLightRotation(12.5f, 45.0f)
            .secondLightRotation(-20.0f, 50.0f)
            .build();

    /**
     * <b>Only use this method outside of subclasses.</b>
     * Use {@link #blockElement(BlockState)} if calling from inside a subclass.
     */
    public static GuiGameElement.GuiRenderBuilder defaultBlockElement(BlockState state) {
        return GuiGameElement.of(state)
                .lighting(DEFAULT_LIGHTING);
    }

    protected GuiGameElement.GuiRenderBuilder blockElement(BlockState state) {
        return defaultBlockElement(state);
    }


    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public void draw(PoseStack poseStack, int xOffset, int yOffset) {

    }
}
