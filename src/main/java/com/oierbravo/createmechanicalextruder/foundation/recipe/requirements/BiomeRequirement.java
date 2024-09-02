package com.oierbravo.createmechanicalextruder.foundation.recipe.requirements;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirement;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirementType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;

import static java.lang.constant.ConstantDescs.NULL;

public class BiomeRequirement extends RecipeRequirement {
    public static final RecipeRequirementType<BiomeRequirement> TYPE = new BiomeRequirementType();


    public static final BiomeRequirement EMPTY = new BiomeRequirement();

    protected TagKey<Biome> biome;

    public BiomeRequirement() {

    }
    public BiomeRequirement(TagKey<Biome> tag) {
        biome = tag;
    }

    public static BiomeRequirement of(TagKey<Biome> tag) {
        return new BiomeRequirement(tag);
    }

    public boolean test(Level pLevel, BlockEntity pBlockEntity) {
        if(biome == null)
            return true;
        Holder<Biome> blockEntityBiome = pLevel.getBiome(pBlockEntity.getBlockPos());

        if(pLevel.isClientSide()){
            return pLevel.registryAccess().registryOrThrow(Registries.BIOME).getTag(biome).map(t -> t.contains(blockEntityBiome)).orElse(false);
        }
        return pLevel.getServer().registryAccess().registryOrThrow(Registries.BIOME).getTag(biome).map(t ->
            t.contains(blockEntityBiome)
        ).orElse(false);
    }

    public boolean isPresent(){
        if(biome == null)
            return false;
        return true;
    }

    public String toString(){
        if(biome == null)
            return null;
        return biome.location().toString();
    }


    @Override
    public RecipeRequirementType<?> getType() {
        return TYPE;
    }

    private static class BiomeRequirementType extends RecipeRequirementType<BiomeRequirement> {

        public BiomeRequirementType() {
            super("biome");
        }

        public BiomeRequirementType(String id) {
            super(id);
        }

        @Override
        public BiomeRequirement fromJson(JsonObject pJson) {
            if (GsonHelper.isValidNode(pJson, "biome")) {
                return BiomeRequirement.of(TagKey.codec(Registries.BIOME).parse(JsonOps.INSTANCE, pJson.get("biome")).result().get());
            }
            return BiomeRequirement.EMPTY;
        }

        @Override
        public JsonObject toJson(JsonObject pJson, RecipeRequirement pRecipeRequirement){
            if(!pRecipeRequirement.isPresent())
                return pJson;
            pJson.addProperty("biome", pRecipeRequirement.toString());
            return pJson;
        }


        @Override
        public BiomeRequirement fromNetwork(FriendlyByteBuf buffer) {
            ResourceLocation rl = buffer.readResourceLocation();
            if(NULL.equals(rl))return BiomeRequirement.EMPTY;
            else return BiomeRequirement.of(TagKey.create(Registries.BIOME, rl));

        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeRequirement pRecipeRequirement) {
            if(pRecipeRequirement instanceof BiomeRequirement){
                TagKey<Biome> biome = ((BiomeRequirement) pRecipeRequirement).biome;
                buffer.writeResourceLocation(biome != null ? biome.location() : null);
            }

        }
    }
}
