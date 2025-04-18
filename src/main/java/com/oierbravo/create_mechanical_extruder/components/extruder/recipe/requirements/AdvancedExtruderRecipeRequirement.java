package com.oierbravo.create_mechanical_extruder.components.extruder.recipe.requirements;

import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirementType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record AdvancedExtruderRecipeRequirement(boolean advancedSifter) implements IRecipeRequirement {

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        return advancedSifter;
    }

    @Override
    public String getIdString() {
        return "advanced_extruder";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return null;
    }
}
