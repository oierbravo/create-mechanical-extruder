package com.oierbravo.createmechanicalextruder.components.extruder;

import java.util.Map;

public interface IRecipeWithConditions {
    Map<RecipeConditionType<?>, RecipeCondition> getRecipeConditions();
}
