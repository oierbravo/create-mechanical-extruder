package com.oierbravo.createmechanicalextruder.components.extruder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
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

public class BiomeRecipeCondition extends RecipeCondition {
    public static final RecipeConditionType<BiomeRecipeCondition> TYPE = new BiomeConditionType();


    public static final BiomeRecipeCondition EMPTY = new BiomeRecipeCondition();

    protected TagKey<Biome> biome;

    public BiomeRecipeCondition() {

    }
    public BiomeRecipeCondition(TagKey<Biome> tag) {
        biome = tag;
    }

    public static BiomeRecipeCondition fromJson(JsonElement pJson){
        return BiomeRecipeCondition.of(TagKey.codec(Registries.BIOME).parse(JsonOps.INSTANCE,pJson).result().get());
    }

    public static BiomeRecipeCondition of(TagKey<Biome> tag) {
        return new BiomeRecipeCondition(tag);
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
    public RecipeConditionType<?> getType() {
        return TYPE;
    }

    private static class BiomeConditionType extends RecipeConditionType<BiomeRecipeCondition>{

        public BiomeConditionType() {
            super("biome");
        }

        public BiomeConditionType(String id) {
            super(id);
        }

        @Override
        public BiomeRecipeCondition fromJson(JsonObject pJson) {
            if (GsonHelper.isValidNode(pJson, "biome")) {
                return BiomeRecipeCondition.of(TagKey.codec(Registries.BIOME).parse(JsonOps.INSTANCE, pJson.get("biome")).result().get());
            }
            return BiomeRecipeCondition.EMPTY;
        }

        @Override
        public JsonObject toJson(JsonObject pJson, RecipeCondition pRecipeCondition){
            if(!pRecipeCondition.isPresent())
                return pJson;
            pJson.addProperty("biome",pRecipeCondition.toString());
            return pJson;
        }


        @Override
        public BiomeRecipeCondition fromNetwork(FriendlyByteBuf buffer) {
            ResourceLocation rl = buffer.readResourceLocation();
            if(NULL.equals(rl))return BiomeRecipeCondition.EMPTY;
            else return BiomeRecipeCondition.of(TagKey.create(Registries.BIOME, rl));

        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeCondition pRecipeCondition) {
            if(pRecipeCondition instanceof BiomeRecipeCondition){
                TagKey<Biome> biome = ((BiomeRecipeCondition) pRecipeCondition).biome;
                buffer.writeResourceLocation(biome != null ? biome.location() : null);
            }

        }
    }
}
