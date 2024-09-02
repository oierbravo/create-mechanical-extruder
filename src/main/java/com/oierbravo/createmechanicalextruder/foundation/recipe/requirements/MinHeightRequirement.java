package com.oierbravo.createmechanicalextruder.foundation.recipe.requirements;

import com.google.gson.JsonObject;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirementType;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MinHeightRequirement extends RecipeRequirement {
    public static final RecipeRequirementType<?> TYPE = new MinHeightRequirementType();
    public static final MinHeightRequirement EMPTY = new MinHeightRequirement();

    private Integer value;

    public MinHeightRequirement() {

    }

    public MinHeightRequirement(int pValue) {
        value = pValue;
    }

    public static MinHeightRequirement of(int pValue) {
        return new MinHeightRequirement(pValue);
    }


    public boolean test(Level pLevel, BlockEntity pBlockEntity) {
        if(value == null)
            return true;
        BlockPos pos = pBlockEntity.getBlockPos();

        return pos.getCenter().y >= value;
    }

    public boolean isPresent(){
        return value != null;
    }

    public String toString(){
        if(value == null)
            return null;
        return value.toString();
    }
    public int getValue(){
        return value;
    }


    @Override
    public RecipeRequirementType<?> getType() {
        return TYPE;
    }

    private static class MinHeightRequirementType extends RecipeRequirementType<MinHeightRequirement> {

        public MinHeightRequirementType() {
            super("min_height");
        }

        @Override
        public MinHeightRequirement fromJson(JsonObject pJson) {
            if (GsonHelper.isValidNode(pJson, this.getId())) {
                return of(pJson.get(this.getId()).getAsInt());
            }
            return EMPTY;
        }

        @Override
        public JsonObject toJson(JsonObject pJson, RecipeRequirement pRecipeRequirement) {
            if(!pRecipeRequirement.isPresent())
                return pJson;
            pJson.addProperty(this.getId(), pRecipeRequirement.toString());
            return pJson;
        }

        @Override
        public MinHeightRequirement fromNetwork(FriendlyByteBuf buffer) {
            return of(buffer.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeRequirement pRecipeRequirement) {
            if(pRecipeRequirement instanceof MinHeightRequirement){
                buffer.writeInt(((MinHeightRequirement) pRecipeRequirement).getValue());
            }
        }
    }
}
