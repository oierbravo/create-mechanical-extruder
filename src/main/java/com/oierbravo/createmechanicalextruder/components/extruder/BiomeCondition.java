package com.oierbravo.createmechanicalextruder.components.extruder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class BiomeCondition {
    public static final BiomeCondition EMPTY = new BiomeCondition();

    protected Biome biome;

    public static BiomeCondition fromTag(TagKey<Biome> tag) {
        BiomeTagCondition condition = new BiomeTagCondition();
        condition.tag = tag;
        return condition;
    }

    public static BiomeCondition fromBiome(Biome biome) {
        BiomeCondition condition = new BiomeCondition();
        condition.biome = biome;
        return condition;
    }

    public static BiomeCondition fromString(String biome) {
        if(Objects.equals(biome, "Any")){
            return BiomeCondition.EMPTY;
        }

        if(biome.startsWith("#")){
            var tagManager = ForgeRegistries.BIOMES.tags();
            var tagKey = tagManager.createTagKey(new ResourceLocation(biome.replace("#","")));
            var tag = tagManager.getTag(tagKey);
            BiomeTagCondition tagCondition = new BiomeTagCondition();
            tagCondition.tag = tagKey;
            return tagCondition;
        }

        BiomeCondition condition = new BiomeCondition();
        ResourceLocation id = new ResourceLocation(biome);
        condition.biome = ForgeRegistries.BIOMES.getValue(id);
        return condition;
    }


    protected void readInternal(FriendlyByteBuf buffer){
        biome = buffer.readRegistryId();
    };

    protected void writeInternal(FriendlyByteBuf buffer){
        buffer.writeRegistryId(ForgeRegistries.BIOMES, biome);
    };

    protected void readInternal(JsonObject json){
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "name"));
        biome = ForgeRegistries.BIOMES.getValue(id);
        if (biome == null)
            throw new JsonSyntaxException("Unknown biome '" + id + "'");
    };

    protected void writeInternal(JsonObject json){
        json.addProperty("name", getKeyOrThrow(biome)
                .toString());
    };
    public static <V> ResourceLocation getKeyOrThrow(IForgeRegistry<V> registry, V value) {
        ResourceLocation key = registry.getKey(value);
        if (key == null) {
            throw new IllegalArgumentException("Could not get key for value " + value + "!");
        }
        return key;
    }
    public static ResourceLocation getKeyOrThrow(Biome value) {
        ResourceLocation key = ForgeRegistries.BIOMES.getKey(value);
        if (key == null) {
            throw new IllegalArgumentException("Could not get key for value " + value + "!");
        }
        return key;
    }
    public boolean test(Biome b, LevelAccessor pLevel) {
        if (b == null)
            throw new IllegalArgumentException("Biome cannot be null");
        return testInternal(b, pLevel);
    }
    protected boolean testInternal(Biome b, LevelAccessor pLevel){
        if(biome == null)
            return false;
        if (biome.toString() != b.toString())
            return false;
        return true;
    };

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this instanceof BiomeTagCondition);
        writeInternal(buffer);
    }

    public static BiomeCondition read(FriendlyByteBuf buffer) {
        boolean isTagBiome = buffer.readBoolean();
        BiomeCondition biome = isTagBiome ? new BiomeTagCondition() : new BiomeCondition();
        biome.readInternal(buffer);
        return biome;
    }
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        writeInternal(json);
        return json;
    }

    public static boolean isBiomeCondition(@Nullable JsonElement je) {
        if (je == null || je.isJsonNull())
            return false;
        if (!je.isJsonObject())
            return false;
        JsonObject json = je.getAsJsonObject();
        if (json.has("tag"))
            return true;
        else if (json.has("name"))
            return true;
        return false;
    }

    public static BiomeCondition deserialize(@Nullable JsonElement je) {
        if (!isBiomeCondition(je))
            throw new JsonSyntaxException("Invalid biome condition: " + Objects.toString(je));

        JsonObject json = je.getAsJsonObject();
        BiomeCondition condition = json.has("tag") ? new BiomeTagCondition() : new BiomeCondition();
        condition.readInternal(json);

        return condition;
    }
    public String toString(){
        return toStringInternal();
    }

    protected String toStringInternal(){
        if(biome == null)
            return "Any";
        return ForgeRegistries.BIOMES.getKey(biome).toString();
    }

    public static class BiomeTagCondition extends BiomeCondition {

        protected TagKey<Biome> tag;
        protected List<Biome> matchingBiomes;

        @Override
        protected boolean testInternal(Biome b, LevelAccessor pLevel) {
            Registry<Biome> biomeRegistry = pLevel.registryAccess().registryOrThrow(ForgeRegistries.BIOMES.getRegistryKey());
            ResourceKey<Biome> key = biomeRegistry.getResourceKey(b).get();
            boolean result = biomeRegistry.getOrCreateTag(tag).contains(biomeRegistry.getOrCreateHolderOrThrow(key));
           return biomeRegistry.getOrCreateTag(tag).contains(biomeRegistry.getOrCreateHolderOrThrow(key));
        }


        @Override
        protected void readInternal(FriendlyByteBuf buffer) {
            ResourceLocation resourceLocation = buffer.readResourceLocation();
            tag = TagKey.create(Registry.BIOME_REGISTRY, resourceLocation);
        }

        @Override
        protected void writeInternal(FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(tag.location());
        }

        @Override
        protected void readInternal(JsonObject json) {
            ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
            tag = TagKey.create(Registry.BIOME_REGISTRY, resourceLocation);
        }

        @Override
        protected void writeInternal(JsonObject json) {
            json.addProperty("tag", tag.location()
                    .toString());
        }

        @Override
        protected String toStringInternal(){
            return "#" + tag.location().toString();
        }
    }
}
