package com.oierbravo.create_mechanical_extruder.compat.jei;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.oierbravo.create_mechanical_extruder.ModConstants;
import com.oierbravo.create_mechanical_extruder.ModLang;
import com.oierbravo.create_mechanical_extruder.compat.jei.animations.AnimatedBrassExtruder;
import com.oierbravo.create_mechanical_extruder.compat.jei.animations.AnimatedExtruder;
import com.oierbravo.create_mechanical_extruder.components.extruder.recipe.ExtrudingRecipe;
import com.oierbravo.create_mechanical_extruder.register.ModBlocks;
import com.oierbravo.create_mechanical_extruder.register.ModRecipes;
import com.oierbravo.mechanicals.compat.jei.CreateRecipeCategoryBuilder;
import com.oierbravo.mechanicals.compat.jei.RecipeRequirementRenderer;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExtrudingCategory extends CreateRecipeCategory<ExtrudingRecipe> {
    public static final BlockPredicate ANY = new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());

    private AnimatedExtruder extruder = new AnimatedExtruder();
    private AnimatedBrassExtruder brassExtruder = new AnimatedBrassExtruder();

    @SuppressWarnings("unchecked")
    public final static CreateRecipeCategory<ExtrudingRecipe> INFO = CreateRecipeCategoryBuilder
            .builder(ExtrudingRecipe.class)
            .addRecipes(ModRecipes::getAllHolders)
            .catalyst(ModBlocks.MECHANICAL_EXTRUDER)
            .catalyst(ModBlocks.MECHANICAL_BRASS_EXTRUDER)
            .icon(new ItemIcon(() -> new ItemStack(ModBlocks.MECHANICAL_EXTRUDER.asItem())))
            .emptyBackground(177, 85)
            .build(ModConstants.asResource("extruding"), ExtrudingCategory::new);

    public ExtrudingCategory(Info<ExtrudingRecipe> info) {
        super(info);
    }

    private List<ItemStack> unwrapItemstacks(HolderSet<Block> blockHolderSet){
        List<ItemStack> itemStackList = new java.util.ArrayList<>(List.of());
        blockHolderSet.unwrap().ifRight(holders -> holders.forEach(blockHolder -> itemStackList.add(new ItemStack(blockHolder.value()))));
        return itemStackList;

    }
    public static boolean isAny(BlockPredicate predicate) {
        if (predicate == ANY) {
            return true;
        }
        return predicate.blocks().isEmpty() && predicate.properties().isEmpty() && predicate.nbt().isEmpty();
    }

    public static Set<Block> matchedBlocks(BlockPredicate predicate) {
        if (isAny(predicate)) {
            return Set.of();
        }
        final var blocks = Lists.<Holder<Block>>newArrayList();
        if (predicate.blocks().isPresent()) {
            Iterables.addAll(
                    blocks,
                    predicate.blocks().get().unwrap().map(BuiltInRegistries.BLOCK::getOrCreateTag, Function.identity()));
        }
        return blocks.stream().map(Holder::value).collect(Collectors.toSet());
    }

    public static Set<Fluid> matchedFluids(BlockPredicate predicate) {
        return matchedBlocks(predicate).stream()
                .filter(LiquidBlock.class::isInstance)
                .map(it -> it.defaultBlockState().getFluidState())
                .filter(Predicate.not(FluidState::isEmpty))
                .map(FluidState::getType)
                .collect(Collectors.toSet());
    }
    public static Set<FluidStack> matchedFluidStacks(BlockPredicate predicate) {
        return matchedFluids(predicate).stream()
                .map(fluid -> new FluidStack(fluid, 1000))
                .collect(Collectors.toSet());
    }

    public static List<ItemStack> matchedItemStacks(BlockPredicate predicate) {
        if (isAny(predicate)) {
            return List.of();
        }
        return matchedBlocks(predicate).stream()
                .map(Block::asItem)
                .filter(Predicate.not(Items.AIR::equals))
                .distinct()
                .map(Item::getDefaultInstance)
                .toList();
    }
    public void setRecipe(IRecipeLayoutBuilder builder, ExtrudingRecipe recipe, IFocusGroup focuses) {
        int slotIndex = 0;
        int initX = 1;
        int initY = 30;
        int distance = 42;
        for(int index= 0;index < 2;index++ ){
            for(int i= 0;i < matchedItemStacks(recipe.getBlockPredicateIngredients().get(index == 0)).size();i++ ){
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1)
                        .addItemStacks(matchedItemStacks(recipe.getBlockPredicateIngredients().get(index == 0)))
                        .addRichTooltipCallback(addConsumeBlockTooltip(recipe.getConsumeBlocks().get(index == 0)));

                slotIndex++;
            }
            Set<FluidStack> fluidIngredients = matchedFluidStacks(recipe.getBlockPredicateIngredients().get(index == 0));
            for(int i= 0;i < fluidIngredients.size();i++ ){
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1)
                        .addIngredients(NeoForgeTypes.FLUID_STACK,fluidIngredients.stream().toList())
                        .addRichTooltipCallback(addConsumeBlockTooltip(recipe.getConsumeBlocks().get(index == 0)));
                slotIndex++;
            }

        }

        if(recipe.getCatalyst().blocks().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 21, 57)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(matchedItemStacks(recipe.getCatalyst()));
        }

        ProcessingOutput output = recipe.getResult();
        builder.addSlot(RecipeIngredientRole.OUTPUT,  44,67)
                .setBackground(getRenderedSlot(), -1, -1)
                .addRichTooltipCallback(addStochasticTooltip(output))
                .addItemStack(recipe.getResultItemStack());

    }

    private static IRecipeSlotRichTooltipCallback addConsumeBlockTooltip(boolean consume){
        return (view, tooltip) -> {
            if(consume)
                tooltip.add(ModLang.translate("ui.recipe.extruding.consumes_block").component()
                        .withStyle(ChatFormatting.RED));
        };
    }

    public void draw(ExtrudingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        if(recipe.isAdvanced())
            brassExtruder.draw(graphics, 42, 55);
        else
            extruder.draw(graphics, 42, 55);

        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 42, 50); //Output arrow
        RecipeRequirementRenderer.drawRequirements(recipe,graphics, 63,4);

    }
}
