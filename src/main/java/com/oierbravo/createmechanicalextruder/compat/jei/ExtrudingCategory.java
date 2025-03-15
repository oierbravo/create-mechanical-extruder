package com.oierbravo.createmechanicalextruder.compat.jei;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.compat.jei.animations.AnimatedExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import com.oierbravo.createmechanicalextruder.foundation.utility.ModLang;
import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.SpeedRequirement;
import com.oierbravo.mechanical_lemon_lib.register.MechanicalLemonRecipeRequirementTypes;
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
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ExtrudingCategory extends CreateRecipeCategory<ExtrudingRecipe> {
    public static final BlockPredicate ANY = new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());

    private AnimatedExtruder extruder = new AnimatedExtruder();
    public final static ResourceLocation UID = CreateMechanicalExtruder.asResource("extruding");
    public final static RecipeType<ExtrudingRecipe>  TYPE = new mezz.jei.api.recipe.RecipeType<>(UID, ExtrudingRecipe.class);
    public final static Supplier<List<RecipeHolder<ExtrudingRecipe>>> RECIPE_SUPPLIER = ModRecipes::getAllHolders;

    public final static CreateRecipeCategory.Info<ExtrudingRecipe> INFO = new CreateRecipeCategory.Info<>(
            TYPE,
            ModLang.translate("recipe.extruding").component(),
            new EmptyBackground(177, 75),
            new ItemIcon(() -> new ItemStack(ModBlocks.MECHANICAL_EXTRUDER.asItem())),
            ExtrudingCategory.RECIPE_SUPPLIER,
            List.of(ModBlocks.MECHANICAL_EXTRUDER::asStack)
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
        int initX = 12;
        int initY = 30;
        int distance = 42;
        for(int index= 0;index < recipe.getBlockIngredients().size();index++ ){
            for(int i= 0;i < matchedItemStacks(recipe.getBlockIngredients().get(index)).size();i++ ){
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addItemStacks(matchedItemStacks(recipe.getBlockIngredients().get(index)));
                slotIndex++;
            }
            Set<FluidStack> fluidIngredients = matchedFluidStacks(recipe.getBlockIngredients().get(index));
            for(int i= 0;i < fluidIngredients.size();i++ ){
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1).addIngredients(NeoForgeTypes.FLUID_STACK,fluidIngredients.stream().toList());
                slotIndex++;
            }

        }

        if(recipe.getCatalyst().blocks().isPresent()) {
            List<ItemStack> itemStackList = new java.util.ArrayList<>(List.of());
            //recipe.getCatalyst().blocks().get().unwrap().ifLeft(blockTagKey -> blockTagKey.cast())
            recipe.getCatalyst().blocks().get().unwrap().ifRight(holders -> holders.forEach(blockHolder -> itemStackList.add(new ItemStack(blockHolder.value()))));
            builder.addSlot(RecipeIngredientRole.INPUT, 33, 57)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(unwrapItemstacks(recipe.getCatalyst().blocks().get()));
                    //.addItemStack(recipe.getCatalyst().blocks().get().get(0));
        }

        ProcessingOutput output = recipe.getResult();
        builder.addSlot(RecipeIngredientRole.OUTPUT,  130,29)
                .setBackground(getRenderedSlot(), -1, -1)
                .addRichTooltipCallback(addStochasticTooltip(output))
                .addItemStack(recipe.getResultItem());

    }


    public void draw(ExtrudingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 80, 32); //Output arrow
        extruder.draw(graphics, 53, 55);
        drawBonks(recipe, graphics, 55,55);
        //drawBiome(recipe, graphics, 55,65);
        drawMinHeight(recipe, graphics, 55,13);
        drawMaxHeight(recipe, graphics, 55,3);
        drawMinSpeed(recipe, graphics, 100,55);

    }
    protected void drawBonks(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        guiGraphics.drawString(fontRenderer,  ModLang.translate("goggles.bonks",recipe.getRequiredBonks()).string(), x, y, 0xFF808080, false);
    }
    /*protected void drawBiome(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            BiomeRequirement biomeRequirement = recipe.getRequirement(BiomeRequirement.TYPE);
            if(biomeRequirement.isPresent()) {
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;
                guiGraphics.drawString(fontRenderer, biomeRequirement.toString(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }*/
    protected void drawMinHeight(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            Optional<IRecipeRequirement> minHeightRequirement = recipe.getRequirement(MechanicalLemonRecipeRequirementTypes.MIN_Y.get());
            if(minHeightRequirement.isPresent()) {
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;
                guiGraphics.drawString(fontRenderer, minHeightRequirement.get().toTooltipComponent(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }
    protected void drawMaxHeight(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            Optional<IRecipeRequirement> maxHeightRequirement = recipe.getRequirement(MechanicalLemonRecipeRequirementTypes.MAX_Y.get());

            if(maxHeightRequirement.isPresent()) {
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;

                guiGraphics.drawString(fontRenderer, ModLang.translate("ui.recipe_requirement.max_height", maxHeightRequirement.get().toString()).string(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }
    protected void drawMinSpeed(ExtrudingRecipe recipe, GuiGraphics guiGraphics, int x, int y) {
        try {
            Optional<IRecipeRequirement> iRecipeRequirement = recipe.getRequirement(MechanicalLemonRecipeRequirementTypes.SPEED.get());
            if(iRecipeRequirement.isPresent()) {
                SpeedRequirement speedRequirement = (SpeedRequirement) iRecipeRequirement.get();
                Minecraft minecraft = Minecraft.getInstance();

                Font fontRenderer = minecraft.font;
                guiGraphics.drawString(fontRenderer, ModLang.translate("ui.recipe_requirement.min_speed", speedRequirement.toString()).string(), x, y, 0xFF808080, false);
            }
        } catch (Exception ignored){

        }


    }
}
