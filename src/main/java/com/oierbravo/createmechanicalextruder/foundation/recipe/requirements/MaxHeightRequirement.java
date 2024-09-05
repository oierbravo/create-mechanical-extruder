package com.oierbravo.createmechanicalextruder.foundation.recipe.requirements;

import com.google.gson.JsonObject;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirement;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirementType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MaxHeightRequirement extends RecipeRequirement {
    public static final RecipeRequirementType<?> TYPE = new MaxHeightRequirementType();
    public static final MaxHeightRequirement EMPTY = new MaxHeightRequirement();

    private Integer value;

    public MaxHeightRequirement() {}

    public MaxHeightRequirement(int pMaxHeight) {
        value = pMaxHeight;
    }

    public static MaxHeightRequirement of(int pMaxHeight) {
        return new MaxHeightRequirement(pMaxHeight);
    }

    public boolean test(Level pLevel, BlockEntity pBlockEntity) {
        if(value == null)
            return true;
        BlockPos pos = pBlockEntity.getBlockPos();

        return pos.getCenter().y <= value;
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return TYPE;
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

    private static class MaxHeightRequirementType extends RecipeRequirementType<MaxHeightRequirement> {

        public MaxHeightRequirementType() {
            super("max_height");
        }

        @Override
        public MaxHeightRequirement fromJson(JsonObject pJson) {
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
        public MaxHeightRequirement fromNetwork(FriendlyByteBuf buffer) {
            boolean hasRequirement = buffer.readBoolean();
            if(hasRequirement) {
                return of(buffer.readInt());
            }
            return MaxHeightRequirement.EMPTY;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeRequirement pRecipeRequirement) {
            if(pRecipeRequirement == null)
                pRecipeRequirement = new MaxHeightRequirement();
            if(pRecipeRequirement instanceof MaxHeightRequirement){
                buffer.writeBoolean(pRecipeRequirement.isPresent());
                if(pRecipeRequirement.isPresent())
                    buffer.writeInt(((MaxHeightRequirement) pRecipeRequirement).getValue());
            }
        }
    }
}
