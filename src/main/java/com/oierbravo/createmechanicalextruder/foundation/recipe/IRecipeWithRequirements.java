package com.oierbravo.createmechanicalextruder.foundation.recipe;

import java.util.Map;

public interface IRecipeWithRequirements {
    Map<RecipeRequirementType<?>, RecipeRequirement> getRecipeRequirements();
}
