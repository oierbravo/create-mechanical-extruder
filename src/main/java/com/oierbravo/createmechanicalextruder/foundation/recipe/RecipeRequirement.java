package com.oierbravo.createmechanicalextruder.foundation.recipe;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class RecipeRequirement {
    public abstract RecipeRequirementType<?> getType();

    public abstract boolean test(Level pLevel, BlockEntity pBlockEntity);

    public abstract boolean isPresent();

}