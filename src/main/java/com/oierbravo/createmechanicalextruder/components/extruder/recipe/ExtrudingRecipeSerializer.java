package com.oierbravo.createmechanicalextruder.components.extruder.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oierbravo.createmechanicalextruder.ModConstants;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExtrudingRecipeSerializer implements RecipeSerializer<ExtrudingRecipe> {
    public static final ExtrudingRecipeSerializer INSTANCE = new ExtrudingRecipeSerializer();

    public static final StreamCodec<RegistryFriendlyByteBuf, NonNullList<BlockPredicate>> STREAM_CODEC_BLOCK_PREDICATE_LIST = CatnipStreamCodecBuilders.nonNullList(BlockPredicate.STREAM_CODEC,2);//BlockPredicate.STREAM_CODEC.apply(ByteBufCodecs.list(2));
    public static final Codec<NonNullList<BlockPredicate>> CODEC_BLOCK_PREDICATE_LIST = NonNullList.codecOf(BlockPredicate.CODEC);


    public final StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> STREAM_CODEC = StreamCodec.of(this::toNetwork, this::fromNetwork);

    private ExtrudingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        ResourceLocation recipeId = ResourceLocation.STREAM_CODEC.decode(buffer);
        Couple<BlockPredicate> blockPredicateList = Couple.streamCodec(BlockPredicate.STREAM_CODEC).decode(buffer);
        ProcessingOutput result = ProcessingOutput.STREAM_CODEC.decode(buffer);
        int requiredBonks = ByteBufCodecs.INT.decode(buffer);
        boolean isAdvanced = ByteBufCodecs.BOOL.decode(buffer);
        Couple<Boolean> consubleBlocks = Couple.streamCodec(ByteBufCodecs.BOOL).decode(buffer);
        BlockPredicate catalystBlockPredicate = BlockPredicate.STREAM_CODEC.decode(buffer);
        List<IRecipeRequirement> recipeRequirements = IRecipeRequirement.LIST_STREAM_CODEC.decode(buffer);

        return new ExtrudingRecipeBuilder(recipeId)
                .withSingleItemOutput(result)
                .withBlockIngredients(blockPredicateList)
                .requiredBonks(requiredBonks)
                .isAdvanced(isAdvanced)
                .consumeBlocks(consubleBlocks)
                .withCatalyst(catalystBlockPredicate)
                .withRequirements(recipeRequirements)
                .build();
    }

    private void toNetwork(RegistryFriendlyByteBuf buffer, ExtrudingRecipe extrudingRecipe) {
        ResourceLocation.STREAM_CODEC.encode(buffer, extrudingRecipe.getId());
        Couple.streamCodec(BlockPredicate.STREAM_CODEC).encode(buffer, extrudingRecipe.getBlockPredicateIngredients());
        ProcessingOutput.STREAM_CODEC.encode(buffer, extrudingRecipe.getResult());
        ByteBufCodecs.INT.encode(buffer,extrudingRecipe.getRequiredBonks());
        ByteBufCodecs.BOOL.encode(buffer, extrudingRecipe.isAdvanced());
        Couple.streamCodec(ByteBufCodecs.BOOL).encode(buffer, extrudingRecipe.getConsumeBlocks());
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
                            Codec.BOOL.optionalFieldOf("advanced", false).forGetter(ExtrudingRecipe::isAdvanced),
                            Couple.codec(Codec.BOOL).optionalFieldOf("consumeBlocks", Couple.create(false, false)).forGetter(ExtrudingRecipe::getConsumeBlocks),
                            IRecipeRequirement.LIST_CODEC.optionalFieldOf("requirements", List.of()).forGetter(ExtrudingRecipe::getRecipeRequirements),
                            ICondition.LIST_CODEC.optionalFieldOf(ConditionalOps.DEFAULT_CONDITIONS_KEY, List.of()).forGetter(ExtrudingRecipe::getConditions)
                    ).apply(instance, (blockIngredients, processingOutput, catalyst, requiredBonks, isAdvanced, consumeBlocks, requirements, iConditions) -> {
                        ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(ModConstants.asResource(ExtrudingRecipe.Type.ID));

                        builder
                                .withBlockIngredients(blockIngredients)
                                .withSingleItemOutput(processingOutput)
                                .withCatalyst(catalyst)
                                .requiredBonks(requiredBonks)
                                .isAdvanced(isAdvanced)
                                .consumeBlocks(consumeBlocks)
                                .withRequirements(requirements)
                        ;
                        return builder.build();
                    })
    );

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MODID,"extruding");



    @Override
    public @NotNull MapCodec<ExtrudingRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}