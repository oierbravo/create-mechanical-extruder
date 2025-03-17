package com.oierbravo.createmechanicalextruder.compat.jei;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.compat.jei.animations.AnimatedBrassExtruder;
import com.oierbravo.createmechanicalextruder.compat.jei.animations.AnimatedExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import com.oierbravo.createmechanicalextruder.foundation.utility.ModLang;
import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.oierbravo.mechanicals.utility.LibLang;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.createmod.catnip.data.Pair;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

    public final static ResourceLocation UID = CreateMechanicalExtruder.asResource("extruding");
    public final static RecipeType<ExtrudingRecipe>  TYPE = new mezz.jei.api.recipe.RecipeType<>(UID, ExtrudingRecipe.class);
    //public final static Supplier<List<RecipeHolder<ExtrudingRecipe>>> RECIPE_SUPPLIER = ModRecipes::getAllHolders;

    public final static CreateRecipeCategory.Info<ExtrudingRecipe> INFO = new CreateRecipeCategory.Info<>(
            TYPE,
            ModLang.translate("recipe.extruding").component(),
            new EmptyBackground(177, 85),
            new ItemIcon(() -> new ItemStack(ModBlocks.MECHANICAL_EXTRUDER.asItem())),
            ModRecipes::getAllHolders,
            List.of(
                    ModBlocks.MECHANICAL_EXTRUDER::asStack,
                    ModBlocks.MECHANICAL_BRASS_EXTRUDER::asStack
            )
    );


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
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addItemStacks(matchedItemStacks(recipe.getBlockPredicateIngredients().get(index == 0)));
                slotIndex++;
            }
            Set<FluidStack> fluidIngredients = matchedFluidStacks(recipe.getBlockPredicateIngredients().get(index == 0));
            for(int i= 0;i < fluidIngredients.size();i++ ){
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addIngredients(NeoForgeTypes.FLUID_STACK,fluidIngredients.stream().toList());
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
                .addItemStack(recipe.getResultItem());

    }


    public void draw(ExtrudingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        if(recipe.isAdvanced())
            brassExtruder.draw(graphics, 42, 55);
        else
            extruder.draw(graphics, 42, 55);

        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 42, 50); //Output arrow
        drawRequirements(recipe, graphics, 63, 4);

    }

    protected void drawRequirements(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y){
        if(recipe.getRecipeRequirements().isEmpty())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        guiGraphics.drawString(fontRenderer, LibLang.translate("ui.recipe.requirements.title").component().withStyle(), x, y, 0xFFFFFFFF, true);

        int index = 0;
        int distance = 9;
        int offsetX = 5;
        int offsetY = 14;

        List<Pair<Component,Component>> requirementComponents = recipe.getRequirementsTooltips();
        for( Pair<Component,Component> pair : requirementComponents){
            int oneLinerLenght = pair.getSecond().getString().length() + pair.getSecond().getString().length();
            if(oneLinerLenght < 19){
                guiGraphics.drawString(fontRenderer, pair.getFirst().plainCopy().append(" ").append(pair.getSecond()), x + offsetX, y + offsetY + distance * index, 0xFF808080, false);
                index++;
                continue;
            }
            guiGraphics.drawString(fontRenderer, pair.getFirst(), x + offsetX, y + offsetY + distance * index, 0xFF808080, false);
            index++;
            guiGraphics.drawString(fontRenderer, pair.getSecond(), x + offsetX * 2, y + offsetY + distance * index, 0xFF808080, false);
            index++;

        }

    }
}
