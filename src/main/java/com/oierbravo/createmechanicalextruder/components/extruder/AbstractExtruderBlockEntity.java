package com.oierbravo.createmechanicalextruder.components.extruder;

import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import com.oierbravo.createmechanicalextruder.foundation.utility.ModLang;
import com.oierbravo.createmechanicalextruder.infrastructure.config.ModConfigs;
import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.oierbravo.mechanicals.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.mechanicals.foundation.blockEntity.behaviour.RecipeRequirementsBehaviour;
import com.oierbravo.mechanicals.register.MechanicalRecipeRequirementTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class AbstractExtruderBlockEntity extends KineticBlockEntity implements CycleBehavior.CycleBehaviourSpecifics, RecipeRequirementsBehaviour.RecipeRequirementsSpecifics<ExtrudingRecipe> {

    //public ItemStackHandler outputInventory;
    public Lazy<IItemHandler> capability;

    private CycleBehavior extrudingBehaviour;
    private FilteringBehaviour filtering;
    public RecipeRequirementsBehaviour<ExtrudingRecipe> recipeRequirementsBehaviour;
    public float headOffset = 0.44f;

    public final ItemStackHandler outputInventory = new ItemStackHandler(1) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
           return stack;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };
    public AbstractExtruderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    public @Nullable IItemHandler getItemHandler() {
        return outputInventory;
    }

    /*public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MECHANICAL_EXTRUDER.get(),
                (be, context) -> be.getItemHandler()
        );

    }*/
    @Override
    public boolean isSpeedRequirementFulfilled() {
        Optional<ExtrudingRecipe> recipe = getRecipe();
        if(recipe.isEmpty())
            return super.isSpeedRequirementFulfilled();
        if(recipe.get().getRequirement(MechanicalRecipeRequirementTypes.MIN_SPEED.get()).isPresent())
            return recipe.get().getRequirement(MechanicalRecipeRequirementTypes.MIN_SPEED.get()).get().test(level, this);
        if(recipe.get().getRequirement(MechanicalRecipeRequirementTypes.MAX_SPEED.get()).isPresent())
            return recipe.get().getRequirement(MechanicalRecipeRequirementTypes.MAX_SPEED.get()).get().test(level, this);
        return super.isSpeedRequirementFulfilled();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        filtering = new FilteringBehaviour(this, new ExtruderFilterSlotPositioning())
                .forRecipes();
        behaviours.add(filtering);

        int cycleTime = ModConfigs.server().mechanicalExtruder.cycleTime.get();

        extrudingBehaviour = new CycleBehavior(this, cycleTime, true);
        behaviours.add(extrudingBehaviour);

        recipeRequirementsBehaviour = new RecipeRequirementsBehaviour<ExtrudingRecipe>(this);
        behaviours.add(recipeRequirementsBehaviour);

    }
    public CycleBehavior getExtrudingBehaviour() {
        return extrudingBehaviour;
    }
    public RecipeRequirementsBehaviour<ExtrudingRecipe> getRecipeConditionsBehaviour() {
        return recipeRequirementsBehaviour;
    }

    @Override
    public void onCycleCompleted() {

    }

    @Override
    public void onOperationCompletd() {

    }

    @Override
    public float getKineticSpeed() {
        return getSpeed();
    }

    @Override
    public boolean tryProcess(boolean simulate) {
        Optional<ExtrudingRecipe> recipe = getRecipe();
        if(recipe.isEmpty()){
            recipeRequirementsBehaviour.cleanRequirements();
            return false;
        }
        ExtrudingRecipe extrudingRecipe = recipe.get();

        if(!recipeRequirementsBehaviour.checkRequirements(extrudingRecipe))
            return false;

        if(simulate)
            return true;

        ItemStack output = extrudingRecipe.getResult().rollOutput();
        if(outputInventory.getStackInSlot(0).isEmpty()){

            outputInventory.setStackInSlot(0, output);
        } else if(outputInventory.getStackInSlot(0).is(extrudingRecipe.getResult().getStack().getItem())) {
            outputInventory.getStackInSlot(0).grow(output.getCount());
        }
        return true;
    }

    @Override
    public void playSound() {
        AllSoundEvents.MECHANICAL_PRESS_ACTIVATION_ON_BELT.playOnServer(level, worldPosition);
    }

    @Override
    public int getCycles() {
        if(!getRecipe().isPresent())
            return 1;
        return getRecipe().get().getRequiredBonks();
    }

    public float getRenderedPoleOffset(float partialTicks) {
        if (!extrudingBehaviour.isRunning())
            return 0;
        int runningTicks = Math.abs(extrudingBehaviour.getRunningTicks());
        float ticks = Mth.lerp(partialTicks, extrudingBehaviour.getPrevRunningTicks(), runningTicks);
        if (runningTicks < (extrudingBehaviour.getCycleTime() * 2) / 3)
            return (float) Mth.clamp(Math.pow(ticks / extrudingBehaviour.getCycleTime() * 2, 3), 0, 1);
        return Mth.clamp((extrudingBehaviour.getCycleTime() - ticks) / extrudingBehaviour.getCycleTime() * 3, 0, 1);
    }

    public Optional<ExtrudingRecipe> getRecipe() {
        List<ExtrudingRecipe> matchingRecipes = ModRecipes.findRecipesWithMatchingIngredients(this);
        if(matchingRecipes.isEmpty())
            return Optional.empty();

        List<ExtrudingRecipe> matchingRequirementRecipes =  matchingRecipes.stream()
                .filter(extrudingRecipe -> extrudingRecipe.meetsRequirements(this)).toList();

        if(!matchingRequirementRecipes.isEmpty())
            return matchingRequirementRecipes.stream().findFirst();
        return matchingRecipes.stream().findAny();
        /*if(matchingRecipes.isPresent())
            return Optional.of(ModRecipes.findExtruding(this, level).get().value());
        return Optional.empty();*/
    }


    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();

    }

    public FilteringBehaviour getFilter() {
        return filtering;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("OutputInventory", outputInventory.serializeNBT(registries));
        super.write(compound, registries, clientPacket);

    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        outputInventory.deserializeNBT(registries, compound.getCompound("OutputInventory"));
        super.read(compound, registries, clientPacket);

    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);


        int currentBonks = extrudingBehaviour.getCurrentCycle();
        if(currentBonks > 0 && getCycles() > 1){
            ModLang.translate("goggles.bonks",currentBonks)
                    .forGoggles(tooltip, 1);
            added = true;
        }

        boolean addedRequirements = recipeRequirementsBehaviour.addToGoggleTooltip(tooltip, isPlayerSneaking, added);
        if(addedRequirements)
            added = true;

        return added;
    }

    private final Map<Direction, Direction> directionLefttBlockMap =
            Map.of(Direction.NORTH, Direction.WEST,
                   Direction.SOUTH, Direction.EAST,
                   Direction.WEST, Direction.NORTH,
                   Direction.EAST, Direction.SOUTH
                    );
    private final Map<Direction, Direction> directionRightBlockMap =
            Map.of(Direction.NORTH, Direction.EAST,
                    Direction.SOUTH, Direction.WEST,
                    Direction.WEST, Direction.SOUTH,
                    Direction.EAST, Direction.NORTH
            );

    public BlockInWorld getLeftBlockInWorld(){
        BlockPos currentPos = this.getBlockPos();
        Direction localDir = this.getBlockState().getValue(HORIZONTAL_FACING);
        assert this.level != null;
        return new BlockInWorld(this.level,currentPos.relative(directionLefttBlockMap.get(localDir)), false);
    }
    private BlockInWorld getRightBlockInWorld(){
        BlockPos currentPos = this.getBlockPos();
        Direction localDir = this.getBlockState().getValue(HORIZONTAL_FACING);
        assert this.level != null;
        return new BlockInWorld(this.level,currentPos.relative(directionRightBlockMap.get(localDir)), false);
    }


    public BlockInWorld getCatalystBlock() {
        assert this.level != null;
        return new BlockInWorld(this.level,this.getBlockPos().below(), false);
    }


    @Override
    public boolean hasEnoughOutputSpace() {
        if(outputInventory.getStackInSlot(0).getCount() == outputInventory.getStackInSlot(0).getMaxStackSize()){
            return false;
        }
        if(!outputInventory.getStackInSlot(0).isEmpty() && !outputInventory.getStackInSlot(0).is(getRecipe().get().getResult().getStack().getItem())){
            return false;
        }
        return true;
    }

    @Override
    public boolean matchesIngredients(ExtrudingRecipe extrudingRecipe) {
        return extrudingRecipe.match( this);
    }

    public boolean matchesIngredients(RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return matchesIngredients(extrudingRecipeRecipeHolder.value());
    }
    public boolean matchRequirements(RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return extrudingRecipeRecipeHolder.value().meetsRequirements(this);
    }


    public Couple<BlockInWorld> getSideBlocks() {
        return Couple.create(
                this.getLeftBlockInWorld(),
                this.getRightBlockInWorld()
        );
    }


    class ExtruderValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 12, 15.75);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis()
                    .isHorizontal();
        }

    }
}
