package com.oierbravo.createmechanicalextruder.components.extruder;

import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import com.oierbravo.createmechanicalextruder.foundation.utility.ModLang;
import com.oierbravo.createmechanicalextruder.infrastructure.config.ModConfigs;
import com.oierbravo.createmechanicalextruder.register.ModBlockEntities;
import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.oierbravo.mechanical_lemon_lib.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.mechanical_lemon_lib.foundation.blockEntity.behaviour.RecipeRequirementsBehaviour;
import com.oierbravo.mechanical_lemon_lib.register.MechanicalLemonRecipeRequirementTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ExtruderBlockEntity extends KineticBlockEntity implements CycleBehavior.CycleBehaviourSpecifics, RecipeRequirementsBehaviour.RecipeRequirementsSpecifics<ExtrudingRecipe> {

    //public ItemStackHandler outputInventory;
    public Lazy<IItemHandler> capability;

    private CycleBehavior extrudingBehaviour;
    private FilteringBehaviour filtering;
    public RecipeRequirementsBehaviour<ExtrudingRecipe> recipeRequirementsBehaviour;
    public float headOffset = 0.44f;

    private BlockInWorld catalystBlock;

    public final ItemStackHandler outputInventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };
    public ExtruderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        //outputInventory = new ItemStackHandler(1);
        //capability = Lazy.of(outputInventory);
        catalystBlock = getCatalystBlock();
    }

    private @Nullable IItemHandler getItemHandler() {
        return outputInventory;
    }
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MECHANICAL_EXTRUDER.get(),
                (be, context) -> be.getItemHandler()
        );

    }
    @Override
    public boolean isSpeedRequirementFulfilled() {
        Optional<ExtrudingRecipe> recipe = getRecipe();
        if(recipe.isEmpty())
            return super.isSpeedRequirementFulfilled();
        if(recipe.get().getRequirement(MechanicalLemonRecipeRequirementTypes.SPEED.get()).isPresent())
            return recipe.get().getRequirement(MechanicalLemonRecipeRequirementTypes.SPEED.get()).get().test(level, this);
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

        if(!recipeRequirementsBehaviour.checkRequirements(recipe, level, this))
            return false;

        if(simulate)
            return true;

        ItemStack output = recipe.get().getResult().rollOutput();
        if(outputInventory.getStackInSlot(0).isEmpty()){

            outputInventory.setStackInSlot(0, output);
        } else if(outputInventory.getStackInSlot(0).is(recipe.get().getResult().getStack().getItem())) {
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
        if(ModRecipes.findExtruding(this, level).isPresent())
            return Optional.of(ModRecipes.findExtruding(this, level).get().value());
        return Optional.empty();
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
    public Block getLeftBlock(){
        BlockPos currentPos = this.getBlockPos();
        Direction localDir = this.getBlockState().getValue(HORIZONTAL_FACING);
        return this.level.getBlockState(currentPos.relative(directionLefttBlockMap.get(localDir))).getBlock();
    }
    private Block getRightBlock(){
        BlockPos currentPos = this.getBlockPos();
        Direction localDir = this.getBlockState().getValue(HORIZONTAL_FACING);
        return this.level.getBlockState(currentPos.relative(directionRightBlockMap.get(localDir))).getBlock();
    }
    public BlockInWorld getLeftBlockInWorld(){
        BlockPos currentPos = this.getBlockPos();
        Direction localDir = this.getBlockState().getValue(HORIZONTAL_FACING);
        return new BlockInWorld(this.level,currentPos.relative(directionLefttBlockMap.get(localDir)), false);

    }
    private BlockInWorld getRightBlockInWorld(){
        BlockPos currentPos = this.getBlockPos();
        Direction localDir = this.getBlockState().getValue(HORIZONTAL_FACING);
        return new BlockInWorld(this.level,currentPos.relative(directionRightBlockMap.get(localDir)), false);
    }
    private Block getBelowBlock(){
        BlockPos currentPos = this.getBlockPos();
        return this.level.getBlockState(currentPos.below()).getBlock();
    }

    public NonNullList<Ingredient> getItemIngredients() {
        NonNullList<Ingredient> itemIngredients = NonNullList.create();
        Block leftBlock = getLeftBlock();
        Block rightBlock = getRightBlock();

        if(!(leftBlock instanceof LiquidBlock)){
            itemIngredients.add( Ingredient.of(leftBlock.asItem()));
        }
        if(!(rightBlock instanceof LiquidBlock)){
            itemIngredients.add( Ingredient.of(rightBlock.asItem()));
        }

        return itemIngredients;
    }

    /*public NonNullList<FluidIngredient> getFluidIngredients() {
        NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
        Block leftBlock = getLeftBlock();
        Block rightBlock = getRightBlock();

        if(leftBlock instanceof LiquidBlock){
            fluidIngredients.add( FluidIngredient.fromFluid(((LiquidBlock) leftBlock).fluid,1000 ));
        }
        if(rightBlock instanceof LiquidBlock){
            fluidIngredients.add( FluidIngredient.fromFluid(((LiquidBlock) rightBlock).fluid,1000 ));
        }

        return fluidIngredients;
    }*/

    public BlockInWorld getCatalystBlock() {
        assert this.level != null;
        return new BlockInWorld(this.level,this.getBlockPos().below(), false);
    }

    /*public List<String> getAllIngredientsStringList() {
        List<String> list = new ArrayList<>();
        getItemIngredients().forEach(ingredient -> list.add((!ingredient.isEmpty()) ? ingredient.getItems()[0].getItem().toString() : ItemStack.EMPTY.toString()));
        getFluidIngredients().forEach(ingredient -> list.add(ingredient.getMatchingFluidStacks().get(0).getFluid().getFluidType().getDescriptionId()));
        Collections.sort(list);
        return list;
    }*/

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
    public boolean matchIngredients(ExtrudingRecipe extrudingRecipe) {
        return ExtrudingRecipe.match( this, extrudingRecipe);
    }

    public boolean matchIngredients(RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return ExtrudingRecipe.match( this, extrudingRecipeRecipeHolder.value());
    }

    public void checkBlocks() {
        catalystBlock = getCatalystBlock();
    }

    public List<BlockInWorld> getSideBlocks() {
        return List.of(
                this.getLeftBlockInWorld(),
                this.getRightBlockInWorld()
        );
    }

    /*private class ExtruderInventoryHandler extends CombinedInvWrapper {

        public ExtruderInventoryHandler() {
            super(outputInventory);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (outputInventory == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;
            return super.isItemValid(slot, stack);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            notifyUpdate();
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }


    }*/
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
