package com.oierbravo.createmechanicalextruder.foundation.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public abstract class RecipeRequirementType<RR extends RecipeRequirement> {
    private final String id;

    public RecipeRequirementType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract RR fromJson(JsonObject pJson);

    public abstract JsonObject toJson(JsonObject pJson, RecipeRequirement pRecipeRequirement);

    public abstract RR fromNetwork(FriendlyByteBuf buffer);

    public abstract void toNetwork(FriendlyByteBuf buffer, RecipeRequirement pRecipeRequirement);

}
