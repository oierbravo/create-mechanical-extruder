package com.oierbravo.create_mechanical_extruder.components.extruder.recipe.requirements;

import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirementType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record BonksRecipeRequirement(int bonks) implements IRecipeRequirement {

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        return true;
    }

    @Override
    public String getIdString() {
        return "bonks";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(bonks);
    }
}
