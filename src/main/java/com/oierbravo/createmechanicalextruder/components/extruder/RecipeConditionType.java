package com.oierbravo.createmechanicalextruder.components.extruder;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public abstract class RecipeConditionType<RC extends RecipeCondition> {
    private final String id;

    public RecipeConditionType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract RC fromJson(JsonObject pJson);

    public abstract JsonObject toJson(JsonObject pJson, RecipeCondition pRecipeCondition);

    public abstract RC fromNetwork(FriendlyByteBuf buffer);

    public abstract void toNetwork(FriendlyByteBuf buffer, RecipeCondition pRecipeCondition);

}
