package com.oierbravo.createmechanicalextruder.components.extruder.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.AbstractExtruderBlockEntity;
import com.oierbravo.mechanicals.foundation.recipe.BaseRecipe;
import com.oierbravo.mechanicals.foundation.recipe.BaseRecipeParams;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class ExtrudingRecipe extends BaseRecipe<RecipeInput, ExtrudingRecipe.ExtrudingRecipeParams> {
    public static final BlockPredicate ANY_BLOCK = new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());
    public static final BlockPredicate WATER_BLOCK = BlockPredicate.Builder.block().of(Blocks.WATER).build();// new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());

    public static Comparator<? super ExtrudingRecipe> hasCatalyst;
    private ResourceLocation id;
    private Couple<BlockPredicate> blockPredicateIngredients;

    private BlockPredicate catalyst;
    private ProcessingOutput result;

    private int requiredBonks;

    @Override
    public ArrayList<IRecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }

    public ExtrudingRecipe(ExtrudingRecipeParams params) {
        super(params);
        this.id = params.id;
        this.result = params.result;
        this.blockPredicateIngredients = params.blockPredicateIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;
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

    /*public @NotNull NonNullList<BlockPredicate> getBlockIngredients(){
        return blockPredicateIngredients;
    }*/

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.rollOutput();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;

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


    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static <T> boolean hasCatalyst(RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return extrudingRecipeRecipeHolder.value().catalyst.blocks().isPresent();
    }

    public static class Type implements RecipeType<ExtrudingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final RecipeType<ExtrudingRecipe> RECIPE_TYPE = new Type();
        public static final String ID = "extruding";
    }

    public static class ExtrudingRecipeParams extends BaseRecipeParams {
        protected Couple<BlockPredicate> blockPredicateIngredients;
        protected ProcessingOutput result;
        protected BlockPredicate catalyst;


        protected int requiredBonks;

        public ArrayList<IRecipeRequirement> recipeRequirements;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            super(id);
            blockPredicateIngredients = Couple.create(ANY_BLOCK,ANY_BLOCK);
            result = ProcessingOutput.EMPTY;
            catalyst = BlockPredicate.Builder.block().build();
            requiredBonks = 1;
            recipeRequirements = new ArrayList<>();
        }

    }
    public static class Serializer implements RecipeSerializer<ExtrudingRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final StreamCodec<RegistryFriendlyByteBuf, NonNullList<BlockPredicate>> STREAM_CODEC_BLOCK_PREDICATE_LIST = CatnipStreamCodecBuilders.nonNullList(BlockPredicate.STREAM_CODEC,2);//BlockPredicate.STREAM_CODEC.apply(ByteBufCodecs.list(2));
        public static final Codec<NonNullList<BlockPredicate>> CODEC_BLOCK_PREDICATE_LIST = NonNullList.codecOf(BlockPredicate.CODEC);


        public final StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> STREAM_CODEC = StreamCodec.of(this::toNetwork, this::fromNetwork);

        private ExtrudingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ResourceLocation recipeId = ResourceLocation.STREAM_CODEC.decode(buffer);
            Couple<BlockPredicate> blockPredicateList = Couple.streamCodec(BlockPredicate.STREAM_CODEC).decode(buffer);
            ProcessingOutput result = ProcessingOutput.STREAM_CODEC.decode(buffer);
            int requiredBonks = ByteBufCodecs.INT.decode(buffer);
            BlockPredicate catalystBlockPredicate = BlockPredicate.STREAM_CODEC.decode(buffer);
            List<IRecipeRequirement> recipeRequirements = IRecipeRequirement.LIST_STREAM_CODEC.decode(buffer);

            return new ExtrudingRecipeBuilder(recipeId)
                    .withSingleItemOutput(result)
                    .withBlockIngredients(blockPredicateList)
                    .requiredBonks(requiredBonks)
                    .withCatalyst(catalystBlockPredicate)
                    .withRequirements(recipeRequirements)
                    .build();
        }

        private void toNetwork(RegistryFriendlyByteBuf buffer, ExtrudingRecipe extrudingRecipe) {
            ResourceLocation.STREAM_CODEC.encode(buffer, extrudingRecipe.id);
            Couple.streamCodec(BlockPredicate.STREAM_CODEC).encode(buffer, extrudingRecipe.getBlockPredicateIngredients());
            ProcessingOutput.STREAM_CODEC.encode(buffer, extrudingRecipe.getResult());
            ByteBufCodecs.INT.encode(buffer,extrudingRecipe.getRequiredBonks());
            BlockPredicate.STREAM_CODEC.encode(buffer, extrudingRecipe.getCatalyst());
            IRecipeRequirement.LIST_STREAM_CODEC.encode(buffer, extrudingRecipe.getRecipeRequirements());
        }

        public static final MapCodec<ExtrudingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance
                        .group(
                                //CODEC_BLOCK_PREDICATE_LIST.fieldOf("blockIngredients").forGetter(ExtrudingRecipe::getBlockPredicateIngredients),
                                Couple.codec(BlockPredicate.CODEC).fieldOf("blockIngredients").forGetter(ExtrudingRecipe::getBlockPredicateIngredients),
                                ProcessingOutput.CODEC.fieldOf("result").forGetter(ExtrudingRecipe::getResult),
                                BlockPredicate.CODEC.optionalFieldOf("catalyst",BlockPredicate.Builder.block().build()).forGetter(ExtrudingRecipe::getCatalyst),
                                Codec.INT.optionalFieldOf("requiredBonks",1).forGetter(ExtrudingRecipe::getRequiredBonks),
                                IRecipeRequirement.LIST_CODEC.optionalFieldOf("requirements", List.of()).forGetter(ExtrudingRecipe::getRecipeRequirements),
                                ICondition.LIST_CODEC.optionalFieldOf(ConditionalOps.DEFAULT_CONDITIONS_KEY, List.of()).forGetter(ExtrudingRecipe::getConditions)
                        ).apply(instance, (blockIngredients, processingOutput, catalyst, requiredBonks, requirements, iConditions) -> {
                            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(CreateMechanicalExtruder.asResource(Type.ID));


                            builder
                                    .withBlockIngredients(blockIngredients)
                                    .withSingleItemOutput(processingOutput)
                                    .withCatalyst(catalyst)
                                    .requiredBonks(requiredBonks)
                                    .withRequirements(requirements)
                            ;
                            return builder.build();
                        })
        );

        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(CreateMechanicalExtruder.MODID,"extruding");



        @Override
        public @NotNull MapCodec<ExtrudingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public Couple<BlockPredicate> getBlockPredicateIngredients() {
        return blockPredicateIngredients;
    }

   /* public NonNullList<BlockPredicate> getBlockPredicateIngredients() {
        return blockPredicateIngredients;
    }*/

}
