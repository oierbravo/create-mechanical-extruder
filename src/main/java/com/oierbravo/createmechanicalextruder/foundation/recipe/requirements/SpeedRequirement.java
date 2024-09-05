package com.oierbravo.createmechanicalextruder.foundation.recipe.requirements;

import com.google.gson.JsonObject;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirement;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirementType;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SpeedRequirement extends RecipeRequirement {
    public static final RecipeRequirementType<?> TYPE = new SpeedRequirementType();
    public static final SpeedRequirement EMPTY = new SpeedRequirement();

    private Float value;

    public SpeedRequirement() {

    }

    public SpeedRequirement(float pValue) {
        value = pValue;
    }


    @Override
    public boolean test(Level pLevel, BlockEntity pBlockEntity) {
        if(pBlockEntity instanceof KineticBlockEntity){
            return Math.abs(((KineticBlockEntity) pBlockEntity).getSpeed()) >= value;
        }
        return true;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    @Override
    public String toString() {
        if(value == null)
            return null;
        return value.toString();
    }

    public Float getValue() {
        return value;
    }

    public static SpeedRequirement of(float pValue) {
        return new SpeedRequirement(pValue);
    }


    @Override
    public RecipeRequirementType<?> getType() {
        return TYPE;
    }

    private static class SpeedRequirementType extends RecipeRequirementType<SpeedRequirement> {

        public SpeedRequirementType() {
            super("min_speed");
        }

        @Override
        public SpeedRequirement fromJson(JsonObject pJson) {
            if (GsonHelper.isValidNode(pJson, this.getId())) {
                return of(pJson.get(this.getId()).getAsFloat());
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
        public SpeedRequirement fromNetwork(FriendlyByteBuf buffer) {
            boolean hasRequirement = buffer.readBoolean();
            if(hasRequirement) {
                return of(buffer.readFloat());
            }
            return SpeedRequirement.EMPTY;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeRequirement pRecipeRequirement) {
            if(pRecipeRequirement == null)
                pRecipeRequirement = new SpeedRequirement();
            if(pRecipeRequirement instanceof SpeedRequirement){
                buffer.writeBoolean(pRecipeRequirement.isPresent());
                if(pRecipeRequirement.isPresent())
                    buffer.writeFloat(((SpeedRequirement) pRecipeRequirement).getValue());
            }

        }
    }
}
