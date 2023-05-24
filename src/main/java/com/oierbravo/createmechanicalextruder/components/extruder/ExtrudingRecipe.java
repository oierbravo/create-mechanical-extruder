package com.oierbravo.createmechanicalextruder.components.extruder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ExtrudingRecipe implements Recipe<SimpleContainer>, IRecipeTypeInfo {
    public static Comparator<? super ExtrudingRecipe> hasCatalyst;
    private static final Random r = new Random();
    private ResourceLocation id;
    private NonNullList<Ingredient> itemIngredients;
    private NonNullList<FluidIngredient> fluidIngredients;
    private ItemStack catalyst;

    private ProcessingOutput result;

    private int requiredBonks;

    public ExtrudingRecipe(ExtrudingRecipeBuilder.ExtrudingRecipeParams params) {
        this.id = params.id;
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.fluidIngredients = params.fluidIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;
    }



    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return false;
    }

    /**
     * ToDO: Solved in a kind of dirty way!!!
     * @param extruderBlockEntity
     * @param recipe
     * @return
     */
    public static boolean match(ExtruderBlockEntity extruderBlockEntity, ExtrudingRecipe recipe) {
        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;

        boolean filterTest = filter.test(recipe.getResultItem());
        if(!getAllIngredients(recipe).equals(extruderBlockEntity.getAllIngredients()))
            return false;


        if(!recipe.catalyst.isEmpty() && !recipe.catalyst.is(extruderBlockEntity.getCatalystItem()))
            return false;

        if (!filterTest)
            return false;
        return true;
    }
    private static boolean checkBlockInRecipe(Block block, ExtrudingRecipe recipe){
        if(block instanceof LiquidBlock){
            return hasFluidStack(new FluidStack(((LiquidBlock) block).getFluid(),1000),recipe.getFluidIngredients());
        }
        return hasItemStack(new ItemStack(block.asItem()),recipe.getItemIngredients());
    }
    private static boolean hasItemStack(ItemStack itemStack,List<Ingredient> itemIngredients){
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

    public static List<String> getAllIngredients(ExtrudingRecipe recipe) {
        List<String> list = new ArrayList<>();
        recipe.getItemIngredients().forEach(ingredient -> list.add(ingredient.getItems()[0].getItem().toString()));
        recipe.getFluidIngredients().forEach(ingredient -> list.add(ingredient.getMatchingFluidStacks().get(0).getFluid().getRegistryName().toString()));
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
            int requiredBonks = 0;


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

            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks);
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


            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                itemIngredients.add(Ingredient.fromNetwork(buffer));

            size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                fluidIngredients.add(FluidIngredient.read(buffer));

            result = ProcessingOutput.read(buffer);
            catalyst = buffer.readItem();
            requiredBonks = buffer.readInt();


            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks);
            return builder.build();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtrudingRecipe recipe) {
            NonNullList<Ingredient> itemIngredients = recipe.itemIngredients;
            NonNullList<FluidIngredient> fluidIngredients = recipe.fluidIngredients;
            ProcessingOutput result = recipe.result;
            ItemStack catalyst = recipe.catalyst;
            int requiredBonks = recipe.requiredBonks;



            buffer.writeVarInt(itemIngredients.size());
            itemIngredients.forEach(i -> i.toNetwork(buffer));
            buffer.writeVarInt(fluidIngredients.size());
            fluidIngredients.forEach(i -> i.write(buffer));
            result.write(buffer);
            buffer.writeItemStack(catalyst, false);
            buffer.writeInt(requiredBonks);
        }
        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }

    }

}
