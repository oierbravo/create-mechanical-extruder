package com.oierbravo.createmechanicalextruder.components.extruder;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class RecipeCondition {
    abstract RecipeConditionType<?> getType();

    abstract boolean test(Level pLevel, BlockEntity pBlockEntity);

    abstract boolean isPresent();
}