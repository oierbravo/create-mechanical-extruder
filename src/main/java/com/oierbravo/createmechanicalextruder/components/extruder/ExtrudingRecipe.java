package com.oierbravo.createmechanicalextruder.components.extruder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ExtrudingRecipe implements Recipe<SimpleContainer>, IRecipeTypeInfo, IRecipeWithConditions {
    public static Comparator<? super ExtrudingRecipe> hasCatalyst;
    private ResourceLocation id;
    private NonNullList<Ingredient> itemIngredients;
    private NonNullList<FluidIngredient> fluidIngredients;
    private ItemStack catalyst;


    private ProcessingOutput result;

    private int requiredBonks;

    private final Map<RecipeConditionType<?>, RecipeCondition> recipeConditions = new HashMap<>();


    private static final List<RecipeConditionType<?>> enabledRecipeConditions = List.of(
            BiomeRecipeCondition.TYPE
    );

    public ExtrudingRecipe(ExtrudingRecipeBuilder.ExtrudingRecipeParams params) {
        this.id = params.id;
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.fluidIngredients = params.fluidIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;

        params.recipeConditions.forEach(
                recipeCondition -> recipeConditions.put(recipeCondition.getType(), recipeCondition)
        );
    }



    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {

        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return result.rollOutput();
    }


    public static boolean match(ExtruderBlockEntity extruderBlockEntity, ExtrudingRecipe recipe){

        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;
        boolean filterTest = filter.test(recipe.getResultItem(extruderBlockEntity.getLevel().registryAccess()));
        if(!getAllIngredientsStringList(recipe).equals(extruderBlockEntity.getAllIngredientsStringList()))
            return false;

        if(!recipe.catalyst.isEmpty() && !recipe.catalyst.is(extruderBlockEntity.getCatalystItem()))
            return false;

        if (!filterTest)
            return false;
        return true;
    }
    /*private static boolean hasItemStack(ItemStack itemStack,List<Ingredient> itemIngredients){
        if(itemStack.isEmpty())
            return false;
        boolean hasItemStack = false;
        for(int i = 0; i< itemIngredients.size();i++){
            if(itemIngredients.get(i).test(itemStack))
                hasItemStack = true;
        }
        return hasItemStack;
    }
    private static boolean hasFluidStack(FluidStack fluidStack, List<FluidIngredient> fluidIngredients){
        if(fluidStack.isEmpty())
            return false;
        boolean hasFluidStack = false;
        for(int i = 0; i< fluidIngredients.size();i++){
            if(fluidIngredients.get(i).test(fluidStack))
                hasFluidStack = true;
        }
        return hasFluidStack;
    }*/
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
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess pRegistryAccess) {
        return result.rollOutput();
    }

    public ItemStack getResultItem() {
        return result.rollOutput();
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

    public List<RecipeConditionType<?>> getEnabledConditions() {
        return enabledRecipeConditions;
    }

    public Map<RecipeConditionType<?>, RecipeCondition> getRecipeConditions() {
        return recipeConditions;
    }

    public <T extends RecipeCondition> T getCondition(RecipeConditionType<T> type) {
        return (T) recipeConditions.get(type);
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

            ArrayList<RecipeCondition> recipeConditions = new ArrayList<>();

            for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
                if (FluidIngredient.isFluidIngredient(je))
                    fluidIngredients.add(FluidIngredient.deserialize(je));
                else
                    itemIngredients.add(Ingredient.fromJson(je));
            }
            result = ProcessingOutput.deserialize(GsonHelper.getAsJsonObject(json, "result"));

            if(GsonHelper.isValidNode(json,"catalyst")){
                catalyst = ShapedRecipe.itemStackFromJson( GsonHelper.getAsJsonObject(json, "catalyst"));
            }

            if(GsonHelper.isValidNode(json,"requiredBonks")){
                requiredBonks = GsonHelper.getAsInt(json,"requiredBonks");
            }
            ExtrudingRecipe.enabledRecipeConditions.forEach(recipeConditionType -> {
                if (GsonHelper.isValidNode(json, recipeConditionType.getId())) {
                    recipeConditions.add(recipeConditionType.fromJson(json));
                }
            });


            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks)
                    .withConditions(recipeConditions);

            return builder.build();
        }
        public JsonObject toJson(JsonObject pJson, ExtrudingRecipe pRecipe) {
            JsonArray jsonIngredients = new JsonArray();

            pRecipe.itemIngredients.forEach(i -> jsonIngredients.add(i.toJson()));
            pRecipe.fluidIngredients.forEach(i -> jsonIngredients.add(i.serialize()));

            pJson.add("result", pRecipe.result.serialize());

            pJson.add("ingredients", jsonIngredients);

            if(pRecipe.hasCatalyst())
                pJson.add("catalyst", new ProcessingOutput(pRecipe.getCatalyst(),1).serialize());

            if (pRecipe.getRequiredBonks() > 1)
                pJson.addProperty("requiredBonks", pRecipe.getRequiredBonks());

            for (Map.Entry<RecipeConditionType<?>,RecipeCondition> entry : pRecipe.recipeConditions.entrySet()) {
                pJson = entry.getKey().toJson(pJson, entry.getValue());
            }


            return pJson;
        }

        @Override
        public ExtrudingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(id);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
            ProcessingOutput result = ProcessingOutput.EMPTY;
            ItemStack catalyst = ItemStack.EMPTY;
            int requiredBonks = 1;

            List<RecipeCondition> recipeConditions = List.of();


            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                itemIngredients.add(Ingredient.fromNetwork(buffer));

            size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                fluidIngredients.add(FluidIngredient.read(buffer));

            result = ProcessingOutput.read(buffer);
            catalyst = buffer.readItem();
            requiredBonks = buffer.readInt();

            ExtrudingRecipe.enabledRecipeConditions.forEach(recipeConditionType -> {
                recipeConditions.add(recipeConditionType.fromNetwork(buffer));
            });

            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks)
                    .withConditions(recipeConditions);
            return builder.build();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtrudingRecipe pRecipe) {
            NonNullList<Ingredient> itemIngredients = pRecipe.itemIngredients;
            NonNullList<FluidIngredient> fluidIngredients = pRecipe.fluidIngredients;
            ProcessingOutput result = pRecipe.result;
            ItemStack catalyst = pRecipe.catalyst;
            int requiredBonks = pRecipe.requiredBonks;

            buffer.writeVarInt(itemIngredients.size());
            itemIngredients.forEach(i -> i.toNetwork(buffer));
            buffer.writeVarInt(fluidIngredients.size());
            fluidIngredients.forEach(i -> i.write(buffer));
            result.write(buffer);
            buffer.writeItemStack(catalyst, false);
            buffer.writeInt(requiredBonks);

            for (Map.Entry<RecipeConditionType<?>,RecipeCondition> entry : pRecipe.recipeConditions.entrySet()) {
                entry.getKey().toNetwork(buffer, entry.getValue());
            }
        }
    }


}
