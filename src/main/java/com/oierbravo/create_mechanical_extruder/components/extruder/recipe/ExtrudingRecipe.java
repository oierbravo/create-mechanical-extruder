package com.oierbravo.create_mechanical_extruder.components.extruder.recipe;

import com.oierbravo.create_mechanical_extruder.components.extruder.AbstractExtruderBlockEntity;
import com.oierbravo.create_mechanical_extruder.components.extruder.recipe.requirements.AdvancedExtruderRecipeRequirement;
import com.oierbravo.mechanicals.foundation.recipe.AbstractMechanicalRecipe;
import com.oierbravo.mechanicals.foundation.recipe.AbstractMechanicalRecipeParams;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class ExtrudingRecipe extends AbstractMechanicalRecipe<RecipeInput, ExtrudingRecipe.ExtrudingRecipeParams> {
    public static final BlockPredicate ANY_BLOCK = new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());
    public static final BlockPredicate WATER_BLOCK = BlockPredicate.Builder.block().of(Blocks.WATER).build();// new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());

    private Couple<BlockPredicate> blockPredicateIngredients;

    private BlockPredicate catalyst;
    private ProcessingOutput result;

    private int requiredBonks;
    private boolean isAdvanced;
    private Couple<Boolean> consumeBlocks;


    @Override
    public ArrayList<IRecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }

    public ExtrudingRecipe(ExtrudingRecipeParams params) {
        super(params);
        this.result = params.result;
        this.blockPredicateIngredients = params.blockPredicateIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;
        this.isAdvanced = params.isAdvanced;
        this.consumeBlocks = params.consumeBlocks;
        this.recipeRequirements.addAll(params.recipeRequirements);
    }

    private boolean matchIngredient(BlockPredicate blockIngredient, BlockInWorld blockInWorld){
        if(blockInWorld.getState().hasProperty(WATERLOGGED) && blockIngredient.blocks().isPresent()){
            boolean waterLogged = blockInWorld.getState().getValue(WATERLOGGED);
            boolean isWaterIngredient = blockIngredient.blocks().get().get(0).is(ResourceLocation.fromNamespaceAndPath("minecraft", "water"));
            if(waterLogged && isWaterIngredient){
                return true;
            }

        }
        return blockIngredient.matches(blockInWorld);
    }
    public <EXB extends AbstractExtruderBlockEntity> boolean matchIngredients(EXB extruderBlockEntity, Couple<BlockPredicate> blockIngredients){
        Couple<Boolean> matchedIngredients = Couple.create(false, false);

        Couple<BlockInWorld> sideBlocks = extruderBlockEntity.getSideBlocks();

        //Check same ingredients
        if(blockIngredients.getFirst().equals(blockIngredients.getSecond())){
            return matchIngredient(blockIngredients.getFirst(), sideBlocks.getFirst()) && matchIngredient(blockIngredients.getSecond(), sideBlocks.getSecond());
        }


        //Check first
        BlockInWorld blockInWorld = sideBlocks.getFirst();
        ArrayList<BlockPredicate> notMatchedIngredients = new ArrayList<>(List.of());

        for( BlockPredicate blockIngredient : blockIngredients ){
            if(this.matchIngredient(blockIngredient,blockInWorld) && !matchedIngredients.getFirst()){
                matchedIngredients.setFirst(true);
            } else {
                notMatchedIngredients.add(blockIngredient);
            }
        }

        //Check second
        blockInWorld = sideBlocks.getSecond();
        for( BlockPredicate blockIngredient : notMatchedIngredients ){

            if(this.matchIngredient(blockIngredient,blockInWorld)  && !matchedIngredients.getSecond() ){
                matchedIngredients.setSecond(true);
            }
        }
        return matchedIngredients.both(aBoolean -> aBoolean.equals(true));
    }

    public <EXB extends AbstractExtruderBlockEntity> boolean match(EXB extruderBlockEntity){
        if(Objects.requireNonNull(extruderBlockEntity.getLevel()).isClientSide)
            return false;
        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;
        boolean filterTest = filter.test(this.getResultItem(extruderBlockEntity.getLevel().registryAccess()));

        if(!matchIngredients(extruderBlockEntity, this.getBlockPredicateIngredients()))
            return false;

        if(this.catalyst.blocks().isPresent() && !this.catalyst.matches(extruderBlockEntity.getCatalystBlock()))
            return false;

        if (!filterTest)
            return false;
        return true;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return result.rollOutput();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }


    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.rollOutput();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ExtrudingRecipeSerializer.INSTANCE;

    }


    public ItemStack getResultItem() {
        return result.rollOutput();
    }
    public ProcessingOutput getResult(){
        return result;
    }

    public BlockPredicate getCatalyst() {
        return catalyst;
    }

    public int getRequiredBonks() {
        return requiredBonks;
    }

    public boolean isAdvanced(){
        return isAdvanced;
    }

    public boolean notAdvanced(){
        return !isAdvanced;
    }

    public Couple<Boolean> getConsumeBlocks(){
        return consumeBlocks;
    }
    public List<BlockPredicate> getConsumeBlocksList(){
        ArrayList<BlockPredicate> list = new ArrayList<>();
        if(consumeBlocks.getFirst())
            list.add(blockPredicateIngredients.getFirst());
        if(consumeBlocks.getSecond())
            list.add(blockPredicateIngredients.getSecond());
        return list;
    }


    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static <T> boolean hasCatalyst(RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return extrudingRecipeRecipeHolder.value().catalyst.blocks().isPresent();
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<IRecipeRequirement> getJeiRecipeRequirements() {
        ArrayList<IRecipeRequirement> extraRequirements = new ArrayList<>();
        if(isAdvanced())
            extraRequirements.add(new AdvancedExtruderRecipeRequirement(true));
        return Stream.concat(extraRequirements.stream(), super.getJeiRecipeRequirements().stream()).toList();
    }

    public static class Type implements RecipeType<ExtrudingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final RecipeType<ExtrudingRecipe> RECIPE_TYPE = new Type();
        public static final String ID = "extruding";
    }

    public static class ExtrudingRecipeParams extends AbstractMechanicalRecipeParams {
        protected Couple<BlockPredicate> blockPredicateIngredients;
        protected ProcessingOutput result;
        protected BlockPredicate catalyst;


        protected int requiredBonks;
        protected boolean isAdvanced;
        protected Couple<Boolean> consumeBlocks;

        public ArrayList<IRecipeRequirement> recipeRequirements;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            super();
            blockPredicateIngredients = Couple.create(ANY_BLOCK,ANY_BLOCK);
            result = ProcessingOutput.EMPTY;
            catalyst = BlockPredicate.Builder.block().build();
            requiredBonks = 1;
            isAdvanced = false;
            consumeBlocks = Couple.create(false, false);
            recipeRequirements = new ArrayList<>();
        }

    }


    public Couple<BlockPredicate> getBlockPredicateIngredients() {
        return blockPredicateIngredients;
    }

   /* public NonNullList<BlockPredicate> getBlockPredicateIngredients() {
        return blockPredicateIngredients;
    }*/

}
