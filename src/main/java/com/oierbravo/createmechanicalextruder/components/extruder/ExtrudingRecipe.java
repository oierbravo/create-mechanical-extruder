package com.oierbravo.createmechanicalextruder.components.extruder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExtrudingRecipe implements Recipe<SimpleContainer>, IRecipeTypeInfo {
    public static Comparator<? super ExtrudingRecipe> hasCatalyst;
    private ResourceLocation id;
    private NonNullList<Ingredient> itemIngredients;
    private NonNullList<FluidIngredient> fluidIngredients;
    private ItemStack catalyst;


    private ProcessingOutput result;

    private int requiredBonks;
    //private BiomeCondition biomeCondition;

    public ExtrudingRecipe(ExtrudingRecipeBuilder.ExtrudingRecipeParams params) {
        this.id = params.id;
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.fluidIngredients = params.fluidIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;
      //  this.biomeCondition = params.biome;
    }



    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return false;
    }

    public static boolean match(ExtruderBlockEntity extruderBlockEntity, ExtrudingRecipe recipe){
    //    if(!recipe.getBiome().test(extruderBlockEntity.getLevel().getBiome(extruderBlockEntity.getBlockPos()).value(),extruderBlockEntity.getLevel()))
    //        return false;
        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;
        boolean filterTest = filter.test(recipe.getResultItem());
        if(!getAllIngredientsStringList(recipe).equals(extruderBlockEntity.getAllIngredientsStringList()))
            return false;

        if(!recipe.catalyst.isEmpty() && !recipe.catalyst.is(extruderBlockEntity.getCatalystItem()))
            return false;

        if (!filterTest)
            return false;
        return true;
    }

    public boolean hasCatalyst() {
        return !this.getCatalyst().isEmpty();
    }
    public List<Ingredient> getItemIngredients() {
        return itemIngredients;
    }

    public List<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    public static List<String> getAllIngredientsStringList(ExtrudingRecipe recipe) {
        List<String> list = new ArrayList<>();

        recipe.getItemIngredients().forEach(ingredient -> list.add(ingredient.getItems()[0].getItem().toString()));
        recipe.getFluidIngredients().forEach(ingredient -> list.add(ingredient.getMatchingFluidStacks().get(0).getFluid().getFluidType().getDescriptionId()));
        Collections.sort(list);
        return list;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return result.rollOutput();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }


    @Override
    public ItemStack getResultItem() {
        return result.getStack();
    }
    public ProcessingOutput getResult(){
        return result;
    }

    public ItemStack getCatalyst() {
        return catalyst;
    }

    public int getRequiredBonks() {
        return requiredBonks;
    }
    /*public BiomeCondition getBiome(){
        return biomeCondition;
    }*/
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<ExtrudingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "extruding";
    }
    public static class Serializer implements RecipeSerializer<ExtrudingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(CreateMechanicalExtruder.MODID,"extruding");

        @Override
        public ExtrudingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(id);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
            //ItemStack result = ItemStack.EMPTY;
            ProcessingOutput result = ProcessingOutput.EMPTY;
            ItemStack catalyst = ItemStack.EMPTY;
            int requiredBonks = 1;
            //BiomeCondition biomeCondition = BiomeCondition.EMPTY;


            for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
                if (FluidIngredient.isFluidIngredient(je))
                    fluidIngredients.add(FluidIngredient.deserialize(je));
                else
                    itemIngredients.add(Ingredient.fromJson(je));
            }
            result = ProcessingOutput.deserialize(GsonHelper.getAsJsonObject(json, "result"));
            //result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            if(GsonHelper.isValidNode(json,"catalyst")){
                catalyst = ShapedRecipe.itemStackFromJson( GsonHelper.getAsJsonObject(json, "catalyst"));
            }

            if(GsonHelper.isValidNode(json,"requiredBonks")){
                requiredBonks = GsonHelper.getAsInt(json,"requiredBonks");
            }

            /*if(GsonHelper.isValidNode(json,"biome")){
                biomeCondition = BiomeCondition.deserialize(json.get("biome"));
            }*/

            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks);
                 //   .withBiomeCondition(biomeCondition);

            return builder.build();
        }

        @Override
        public ExtrudingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(id);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
            ProcessingOutput result = ProcessingOutput.EMPTY;
            ItemStack catalyst = ItemStack.EMPTY;
            int requiredBonks = 1;
            //BiomeCondition biomeCondition = BiomeCondition.EMPTY;


            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                itemIngredients.add(Ingredient.fromNetwork(buffer));

            size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                fluidIngredients.add(FluidIngredient.read(buffer));

            result = ProcessingOutput.read(buffer);
            catalyst = buffer.readItem();
            requiredBonks = buffer.readInt();

            //biomeCondition = BiomeCondition.read(buffer);

            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks);
                 //   .withBiomeCondition(biomeCondition);
            return builder.build();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtrudingRecipe recipe) {
            NonNullList<Ingredient> itemIngredients = recipe.itemIngredients;
            NonNullList<FluidIngredient> fluidIngredients = recipe.fluidIngredients;
            ProcessingOutput result = recipe.result;
            ItemStack catalyst = recipe.catalyst;
            int requiredBonks = recipe.requiredBonks;

            //BiomeCondition biomeCondition = recipe.biomeCondition;

            buffer.writeVarInt(itemIngredients.size());
            itemIngredients.forEach(i -> i.toNetwork(buffer));
            buffer.writeVarInt(fluidIngredients.size());
            fluidIngredients.forEach(i -> i.write(buffer));
            result.write(buffer);
            buffer.writeItemStack(catalyst, false);
            buffer.writeInt(requiredBonks);
            //biomeCondition.write(buffer);
        }


    }

}
