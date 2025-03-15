package com.oierbravo.createmechanicalextruder.components.extruder.recipe;

import com.mojang.datafixers.util.Either;
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
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExtrudingRecipe extends BaseRecipe<RecipeInput, ExtrudingRecipe.ExtrudingRecipeParams> {

    public static Comparator<? super ExtrudingRecipe> hasCatalyst;
    private ResourceLocation id;
    private NonNullList<Ingredient> itemIngredients;
    private NonNullList<FluidIngredient> fluidIngredients;
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
        this.itemIngredients = params.itemIngredients;
        this.fluidIngredients = params.fluidIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;
        this.recipeRequirements.addAll(params.recipeRequirements);
    }



    public static boolean match(ExtruderBlockEntity extruderBlockEntity, ExtrudingRecipe recipe){
        if(extruderBlockEntity.getLevel().isClientSide)
            return false;
        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;
        boolean filterTest = filter.test(recipe.getResultItem(extruderBlockEntity.getLevel().registryAccess()));
        if(!getAllIngredientsStringList(recipe).equals(extruderBlockEntity.getAllIngredientsStringList()))
            return false;
        BlockInWorld b = extruderBlockEntity.getCatalystBlock();
        Block bb = extruderBlockEntity.getLevel().getBlockState(extruderBlockEntity.getBlockPos().below()).getBlock();
        if(recipe.catalyst.blocks().isPresent() && !recipe.catalyst.matches(extruderBlockEntity.getCatalystBlock()))
            return false;

        if (!filterTest)
            return false;
        return true;
    }

    public boolean hasCatalyst() {
        return this.getCatalyst().blocks().isPresent();
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

    /*@Override
    public @NotNull Map<RecipeRequirementType<?>, IRecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }*/
    /*public List<IRecipeRequirement> getRequirements(){
        return getRecipeRequirements();
    }*/
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
        protected NonNullList<Ingredient> itemIngredients;
        protected ProcessingOutput result;
        protected NonNullList<FluidIngredient> fluidIngredients;
        protected BlockPredicate catalyst;


        protected int requiredBonks;

        public ArrayList<IRecipeRequirement> recipeRequirements;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            super(id);
            itemIngredients = NonNullList.create();
            result = ProcessingOutput.EMPTY;
            fluidIngredients = NonNullList.create();
            catalyst = BlockPredicate.Builder.block().build();
            requiredBonks = 1;
            recipeRequirements = new ArrayList<>();
        }

    }
    public static class Serializer extends BaseRecipeSerializer<ExtrudingRecipe, ExtrudingRecipeBuilder> implements RecipeSerializer<ExtrudingRecipe> {
        public static final Serializer INSTANCE = new Serializer(ExtrudingRecipe.enabledRecipeRequirements);

        public final StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> STREAM_CODEC = StreamCodec.of(this::toNetwork, this::fromNetwork);

        private ExtrudingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ResourceLocation recipeId = ResourceLocation.STREAM_CODEC.decode(buffer);

            NonNullList<Ingredient> ingredients = CatnipStreamCodecBuilders.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).decode(buffer);
            NonNullList<FluidIngredient> fluidIngredients = CatnipStreamCodecBuilders.nonNullList(FluidIngredient.STREAM_CODEC).decode(buffer);
            ProcessingOutput result = ProcessingOutput.STREAM_CODEC.decode(buffer);
            int requiredBonks = ByteBufCodecs.INT.decode(buffer);
            BlockPredicate catalystBlockPredicate = BlockPredicate.STREAM_CODEC.decode(buffer);
            List<IRecipeRequirement> recipeRequirements = IRecipeRequirement.LIST_STREAM_CODEC.decode(buffer);

            return (ExtrudingRecipe) new ExtrudingRecipeBuilder(recipeId).withItemIngredients(ingredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .requiredBonks(requiredBonks)
                    .withCatalyst(catalystBlockPredicate)
                    .withRequirements(recipeRequirements)
                    //.withRequirement(SpeedRequirement.of(requiredSpeed))
                    .build();
        }

        private void toNetwork(RegistryFriendlyByteBuf buffer, ExtrudingRecipe extrudingRecipe) {
            ResourceLocation.STREAM_CODEC.encode(buffer, extrudingRecipe.id);

            CatnipStreamCodecBuilders.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).encode(buffer, extrudingRecipe.itemIngredients);
            CatnipStreamCodecBuilders.nonNullList(FluidIngredient.STREAM_CODEC).encode(buffer, extrudingRecipe.fluidIngredients);
            ProcessingOutput.STREAM_CODEC.encode(buffer, extrudingRecipe.getResult());
            ByteBufCodecs.INT.encode(buffer,extrudingRecipe.getRequiredBonks());
            BlockPredicate.STREAM_CODEC.encode(buffer, extrudingRecipe.getCatalyst());
            IRecipeRequirement.LIST_STREAM_CODEC.encode(buffer, extrudingRecipe.getRecipeRequirements());
        }

        public static final MapCodec<ExtrudingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance
                        .group(
                                Codec.either(Ingredient.CODEC, FluidIngredient.CODEC).listOf().fieldOf("ingredients").forGetter(i -> {
                                    List<Either<Ingredient, FluidIngredient>> list = new ArrayList<>();
                                    i.getIngredients().forEach(o -> list.add(Either.left(o)));
                                    i.getFluidIngredients().forEach(o -> list.add(Either.right(o)));
                                    return list;
                                }),

                                ProcessingOutput.CODEC.fieldOf("result").forGetter(ExtrudingRecipe::getResult),
                                BlockPredicate.CODEC.optionalFieldOf("catalyst",BlockPredicate.Builder.block().build()).forGetter(ExtrudingRecipe::getCatalyst),
                                Codec.INT.optionalFieldOf("requiredBonks",1).forGetter(ExtrudingRecipe::getRequiredBonks),
                                IRecipeRequirement.LIST_CODEC.optionalFieldOf("requirements", List.of()).forGetter(ExtrudingRecipe::getRecipeRequirements),
                                ICondition.LIST_CODEC.optionalFieldOf(ConditionalOps.DEFAULT_CONDITIONS_KEY, List.of()).forGetter(ExtrudingRecipe::getConditions)
                        ).apply(instance, (ingredients, processingOutput, catalyst, requiredBonks, requirements, iConditions) -> {
                        //).apply(instance, (ingredients, processingOutput, catalyst, catalystBlock, catalystBlockState, blockPredicate, requiredBonks, requirements, iConditions) -> {
                            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(CreateMechanicalExtruder.asResource(Type.ID));

                            NonNullList<Ingredient> ingredientList = NonNullList.create();
                            NonNullList<FluidIngredient> fluidIngredientList = NonNullList.create();

                            for (Either<Ingredient, FluidIngredient> either : ingredients) {
                                either.left().ifPresent(ingredientList::add);
                                either.right().ifPresent(fluidIngredientList::add);
                            }

                            builder
                                    .withItemIngredients(ingredientList)
                                    .withFluidIngredients(fluidIngredientList)
                                    .withSingleItemOutput(processingOutput)
                                    //.withCatalystPredicate(blockPredicate)
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

    private ResourceLocation getId() {
        return id;
    }
}
