package com.oierbravo.createmechanicalextruder.foundation.recipe.requirements;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirement;
import com.oierbravo.createmechanicalextruder.foundation.recipe.RecipeRequirementType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

import static java.lang.constant.ConstantDescs.NULL;

public class BiomeRequirement extends RecipeRequirement {
    public static final RecipeRequirementType<BiomeRequirement> TYPE = new BiomeRequirementType();


    public static final BiomeRequirement EMPTY = new BiomeRequirement();

    protected TagKey<Biome> biomeTagKey;

    protected ResourceKey<Biome> biomeResourceKey;

    public BiomeRequirement() {

    }
    public BiomeRequirement(TagKey<Biome> tag) {
        biomeTagKey = tag;
    }

    public BiomeRequirement(ResourceKey<Biome> key) {
        biomeResourceKey = key;
    }

    public BiomeRequirement(ResourceKey<Biome> key, TagKey<Biome> tag) {
        this.biomeResourceKey = key;
        this.biomeTagKey = tag;
    }
    public static BiomeRequirement of(ResourceKey<Biome> key) {
        return new BiomeRequirement(key);
    }
    public static BiomeRequirement of(TagKey<Biome> tag) {
        return new BiomeRequirement(tag);
    }
    public static BiomeRequirement of(ResourceKey<Biome> key, TagKey<Biome> tag) {
        return new BiomeRequirement(key, tag);
    }

    public boolean test(Level pLevel, BlockEntity pBlockEntity) {
        if(biomeResourceKey == null && biomeTagKey == null)
            return true;
        Holder<Biome> blockEntityBiome = pLevel.getBiome(pBlockEntity.getBlockPos());

        if(pLevel.isClientSide()){
            return false;
        }

        Optional<Holder.Reference<Biome>> requiredBiomeHolder = pLevel.getServer().registryAccess().registryOrThrow(Registries.BIOME).asLookup().get(biomeResourceKey);

        if(requiredBiomeHolder.isPresent()
           && blockEntityBiome.is(requiredBiomeHolder.get().key()))
            return true;


        return pLevel.getServer().registryAccess().registryOrThrow(Registries.BIOME).getTag(biomeTagKey).map(t ->
            t.contains(blockEntityBiome)
        ).orElse(false);
    }

    public boolean isPresent(){
        if(biomeTagKey == null && biomeResourceKey == null)
            return false;
        return true;
    }

    public String toString(){
        if(biomeTagKey == null && biomeResourceKey == null)
            return null;
        if(biomeResourceKey != null)
            return biomeResourceKey.location().toString();
        return biomeTagKey.location().toString();
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
                ResourceKey<Biome> biomeResourceKey = ResourceKey.codec(Registries.BIOME).parse(JsonOps.INSTANCE, pJson.get("biome")).result().get();
                TagKey<Biome> biomeTag = TagKey.codec(Registries.BIOME).parse(JsonOps.INSTANCE, pJson.get("biome")).result().get();
                return BiomeRequirement.of(biomeResourceKey,biomeTag);
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
            boolean hasRequirement = buffer.readBoolean();
            if(hasRequirement) {
                ResourceLocation rl = buffer.readResourceLocation();
                if (NULL.equals(rl)) return BiomeRequirement.EMPTY;
                else return BiomeRequirement.of(TagKey.create(Registries.BIOME, rl));
            }
            return BiomeRequirement.EMPTY;
        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeRequirement pRecipeRequirement) {
            if(pRecipeRequirement == null)
                pRecipeRequirement = new BiomeRequirement();
            if(pRecipeRequirement instanceof BiomeRequirement){
                buffer.writeBoolean(pRecipeRequirement.isPresent());
                if(pRecipeRequirement.isPresent()) {
                    TagKey<Biome> biome = ((BiomeRequirement) pRecipeRequirement).biomeTagKey;
                    buffer.writeResourceLocation(biome.location());
                }
            }
        }
    }
}
