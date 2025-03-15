package com.oierbravo.createmechanicalextruder.components.extruder.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderBlockEntity;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.BaseRecipe;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.BaseRecipeParams;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.BaseRecipeSerializer;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.SpeedRequirement;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExtrudingRecipe extends BaseRecipe<RecipeInput, ExtrudingRecipe.ExtrudingRecipeParams> {

    public static Comparator<? super ExtrudingRecipe> hasCatalyst;
    private ResourceLocation id;
    private NonNullList<BlockPredicate> blockPredicateIngredients;

    private BlockPredicate catalyst;
    private ProcessingOutput result;

    private int requiredBonks;

    //private final Map<String, IRecipeRequirement> recipeRequirements = new HashMap<>();


    public static final List<String> enabledRecipeRequirements = List.of(
         //   BiomeRequirement.TYPE,
         //   MinHeightRequirement.TYPE,
         //   MaxHeightRequirement.TYPE,
            SpeedRequirement.ID
            //"min_speed"
    );

    @Override
    public ArrayList<IRecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }

    @Override
    public List<String> getEnabledRequirements() {
        return enabledRecipeRequirements;
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


    public static boolean matchIngredients(ExtruderBlockEntity extruderBlockEntity,List<BlockPredicate> blockIngredients){
        List<BlockPredicate> matchedIngredients = new ArrayList<>();
        List<BlockInWorld> sideBlocks = extruderBlockEntity.getSideBlocks();
            for( BlockPredicate blockIngredient : blockIngredients ){
                for(int i = 0; i < sideBlocks.size(); i++){
                    if(blockIngredient.matches(sideBlocks.get(i))){
                        matchedIngredients.add(blockIngredient);
                        break;
                    }
                }
            }
        return matchedIngredients.size() == 2;
    }
    public static boolean match(ExtruderBlockEntity extruderBlockEntity, ExtrudingRecipe recipe){
        if(extruderBlockEntity.getLevel().isClientSide)
            return false;
        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;
        boolean filterTest = filter.test(recipe.getResultItem(extruderBlockEntity.getLevel().registryAccess()));

        if(!matchIngredients(extruderBlockEntity, recipe.getBlockIngredients()))
            return false;

        if(recipe.catalyst.blocks().isPresent() && !recipe.catalyst.matches(extruderBlockEntity.getCatalystBlock()))
            return false;

        if (!filterTest)
            return false;
        return true;
    }

    public boolean hasCatalyst() {
        return this.getCatalyst().blocks().isPresent();
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

    public @NotNull NonNullList<BlockPredicate> getBlockIngredients(){
        return blockPredicateIngredients;
    }

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

    @Override
    public boolean checkRequirements(Level level, BlockEntity blockEntity) {
        return false;
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
        protected NonNullList<BlockPredicate> blockPredicateIngredients;
        protected ProcessingOutput result;
        protected BlockPredicate catalyst;


        protected int requiredBonks;

        public ArrayList<IRecipeRequirement> recipeRequirements;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            super(id);
            blockPredicateIngredients = NonNullList.create();
            result = ProcessingOutput.EMPTY;
            catalyst = BlockPredicate.Builder.block().build();
            requiredBonks = 1;
            recipeRequirements = new ArrayList<>();
        }

    }
    public static class Serializer extends BaseRecipeSerializer<ExtrudingRecipe, ExtrudingRecipeBuilder> implements RecipeSerializer<ExtrudingRecipe> {
        public static final Serializer INSTANCE = new Serializer(ExtrudingRecipe.enabledRecipeRequirements);

        public static final StreamCodec<RegistryFriendlyByteBuf, NonNullList<BlockPredicate>> STREAM_CODEC_BLOCK_PREDICATE_LIST = CatnipStreamCodecBuilders.nonNullList(BlockPredicate.STREAM_CODEC,2);//BlockPredicate.STREAM_CODEC.apply(ByteBufCodecs.list(2));
        public static final Codec<NonNullList<BlockPredicate>> CODEC_BLOCK_PREDICATE_LIST = NonNullList.codecOf(BlockPredicate.CODEC);


        public final StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> STREAM_CODEC = StreamCodec.of(this::toNetwork, this::fromNetwork);

        private ExtrudingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ResourceLocation recipeId = ResourceLocation.STREAM_CODEC.decode(buffer);
            NonNullList<BlockPredicate> blockPredicateList = CatnipStreamCodecBuilders.nonNullList(BlockPredicate.STREAM_CODEC,2).decode(buffer);
            ProcessingOutput result = ProcessingOutput.STREAM_CODEC.decode(buffer);
            int requiredBonks = ByteBufCodecs.INT.decode(buffer);
            BlockPredicate catalystBlockPredicate = BlockPredicate.STREAM_CODEC.decode(buffer);
            List<IRecipeRequirement> recipeRequirements = IRecipeRequirement.LIST_STREAM_CODEC.decode(buffer);

            return (ExtrudingRecipe) new ExtrudingRecipeBuilder(recipeId)
                    .withSingleItemOutput(result)
                    .withBlockIngredients(blockPredicateList)
                    .requiredBonks(requiredBonks)
                    .withCatalyst(catalystBlockPredicate)
                    .withRequirements(recipeRequirements)
                    .build();
        }

        private void toNetwork(RegistryFriendlyByteBuf buffer, ExtrudingRecipe extrudingRecipe) {
            ResourceLocation.STREAM_CODEC.encode(buffer, extrudingRecipe.id);
            CatnipStreamCodecBuilders.nonNullList(BlockPredicate.STREAM_CODEC,2).encode(buffer, extrudingRecipe.getBlockPredicateIngredients());
            ProcessingOutput.STREAM_CODEC.encode(buffer, extrudingRecipe.getResult());
            ByteBufCodecs.INT.encode(buffer,extrudingRecipe.getRequiredBonks());
            BlockPredicate.STREAM_CODEC.encode(buffer, extrudingRecipe.getCatalyst());
            IRecipeRequirement.LIST_STREAM_CODEC.encode(buffer, extrudingRecipe.getRecipeRequirements());
        }

        public static final MapCodec<ExtrudingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance
                        .group(
                                CODEC_BLOCK_PREDICATE_LIST.fieldOf("blockIngredients").forGetter(ExtrudingRecipe::getBlockPredicateIngredients),
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

        public Serializer(List<String> pEnabledRecipeRequirements) {
            super(pEnabledRecipeRequirements);
        }

        @Override
        public @NotNull MapCodec<ExtrudingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public NonNullList<BlockPredicate> getBlockPredicateIngredients() {
        return blockPredicateIngredients;
    }

}
