package com.oierbravo.createmechanicalextruder.compat.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.fluid.UnboundFluidStackJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class ExtrudingRecipeJS extends RecipeJS {
    public final List<FluidIngredient> fluidIngredients = new ArrayList<>();

    ItemStack outputItem;
    ItemStack catalyst;
    public ExtrudingRecipeJS() {
    }
    @Override
    public void create(ListJS args) {
        outputItem = ItemStackJS.of(args.get(0)).getItemStack();
        for (var element : ListJS.orSelf(args.get(1))) {
            if(element instanceof UnboundFluidStackJS){
                UnboundFluidStackJS unboundFluidStackJS = (UnboundFluidStackJS) element;
                fluidIngredients.add(FluidIngredient.fromFluid(unboundFluidStackJS.getFluid(), (int) unboundFluidStackJS.getAmount()));
            } else {
                inputItems.add( IngredientJS.of(element));
            }
        }
        catalyst = ItemStack.EMPTY;

    }



    @Override
    public void deserialize() {
        for (var ingredient : json.get("ingredients").getAsJsonArray()) {
            if (FluidIngredient.isFluidIngredient(ingredient)) {
                fluidIngredients.add(FluidIngredient.deserialize(ingredient));
            } else {
                inputItems.add(parseIngredientItem(ingredient));
            }
        }
        outputItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        if(GsonHelper.isValidNode(json,"catalyst")){
            catalyst = ShapedRecipe.itemStackFromJson( GsonHelper.getAsJsonObject(json, "catalyst"));
        }
    }

    @Override
    public void serialize() {
            var jsonIngredients = new JsonArray();
            for (var inputStack : inputItems) {
                for (var ingredient : inputStack.unwrapStackIngredient()) {
                    jsonIngredients.add(ingredient.toJson());
                }
            }

            for (var fluid : fluidIngredients) {
                jsonIngredients.add(fluid.serialize());
            }

        if(!catalyst.isEmpty()){
                json.add("catalyst", itemToJson(catalyst));
            }
            json.add("ingredients", jsonIngredients);

            json.add("result", itemToJson(outputItem));

    }

    private JsonElement itemToJson(ItemStack itemStack) {
        return new ItemStackJS(itemStack).toResultJson();

    }

    public ExtrudingRecipeJS withCatalyst(ItemStack catalyst) {
        json.add("catalyst", itemToJson(catalyst));
        save();
        return this;
    }

}
