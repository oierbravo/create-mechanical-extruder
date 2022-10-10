package com.oierbravo.createmechanicalextruder.compat.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.fluid.UnboundFluidStackJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.rhino.NativeArray;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExtrudingRecipeJS extends RecipeJS {
    public final List<FluidIngredient> fluidIngredients = new ArrayList<>();
    public List<Ingredient> itemIngredients = new ArrayList<>();

    ItemStack outputItem;
    ItemStack catalyst;
    @Override
    public void create(RecipeArguments recipeArguments) {
        outputItem = ItemStackJS.of(recipeArguments.get(0));
        parseIngredientsList(recipeArguments.get(1));
        catalyst = ItemStack.EMPTY;

    }
    public void parseIngredientsList(@Nullable Object o){
    /*    if (o instanceof JsonElement elem) {
            var array = elem instanceof JsonArray arr ? arr : Util.make(new JsonArray(), (arr) -> arr.add(elem));
            for (var e : array) {
                if(e.isJsonObject() && GsonHelper.isValidNode((JsonObject) e,"fluid") || GsonHelper.isValidNode((JsonObject) e,"fluidTag"))
                    fluidIngredients.add(FluidIngredient.deserialize(e));
                else
                    itemIngredients.add(parseItemInput(e));
            }
        }*/
        if(o instanceof NativeArray){
            for(int i = 0; i < ((NativeArray) o).size(); i++){
                Object element = ((NativeArray) o).get(i);
                if(element instanceof UnboundFluidStackJS){
                    UnboundFluidStackJS unboundFluidStackJS = (UnboundFluidStackJS) element;
                   fluidIngredients.add(FluidIngredient.fromFluid(unboundFluidStackJS.getFluid(), (int) unboundFluidStackJS.getAmount()));
                } else {
                    itemIngredients.add( Ingredient.of((ItemStack) element));
                }
            }
        }

    }


    @Override
    public void deserialize() {
        for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
            if (FluidIngredient.isFluidIngredient(je))
                fluidIngredients.add(FluidIngredient.deserialize(je));
            else
                itemIngredients.add(Ingredient.fromJson(je));
        }
        outputItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        if(GsonHelper.isValidNode(json,"catalyst")){
            catalyst = ShapedRecipe.itemStackFromJson( GsonHelper.getAsJsonObject(json, "catalyst"));
        }
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            var jsonIngredients = new JsonArray();
            for (var item : itemIngredients) {
                jsonIngredients.add(item.toJson());
            }
            for (var fluid : fluidIngredients) {
                jsonIngredients.add(fluid.serialize());
            }
            if(!catalyst.isEmpty()){
                json.add("catalyst", itemToJson(catalyst));
            }
            json.add("ingredients", jsonIngredients);
        }
        if (serializeOutputs) {
            json.add("result", itemToJson(outputItem));
        }

    }
    public ExtrudingRecipeJS withCatalyst(ItemStack catalyst) {
        json.add("catalyst", itemToJson(catalyst));
        save();
        return this;
    }
    @Override
    public boolean hasInput(IngredientMatch ingredientMatch) {
        for (var in : itemIngredients) {
            if (ingredientMatch.contains(in)) {
                return true;
            }
        }
        if(ingredientMatch.contains(catalyst))
            return true;
        return false;

    }

    @Override
    public boolean replaceInput(IngredientMatch ingredientMatch, Ingredient ingredient, ItemInputTransformer itemInputTransformer) {
        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch ingredientMatch) {
        return ingredientMatch.contains(outputItem);
    }

    @Override
    public boolean replaceOutput(IngredientMatch ingredientMatch, ItemStack itemStack, ItemOutputTransformer itemOutputTransformer) {
        if (ingredientMatch.contains(outputItem)) {
            outputItem = itemOutputTransformer.transform(this, ingredientMatch, outputItem, itemStack);
            return true;
        }
        return false;
    }
}
