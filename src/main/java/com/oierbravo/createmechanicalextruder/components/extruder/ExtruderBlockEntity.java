package com.oierbravo.createmechanicalextruder.components.extruder;

import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.*;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ExtruderBlockEntity extends KineticBlockEntity implements ExtrudingBehaviour.ExtrudingBehaviourSpecifics {
    public ItemStackHandler outputInv;
    public LazyOptional<IItemHandler> capability;
    public int timer;
    //private ExtruderRecipe lastRecipe;
    //private boolean contentsChanged;

    public ExtrudingBehaviour extrudingBehaviour;
    private FilteringBehaviour filtering;


    public ExtruderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        outputInv = new ItemStackHandler(1);
        capability = LazyOptional.of(ExtruderInventoryHandler::new);
        //contentsChanged = true;
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        //filtering = new FilteringBehaviour(this, new ExtruderTileEntity.ExtruderValueBox()).moveText(new Vec3(2, -8, 0))
            //    .withCallback(newFilter -> contentsChanged = true)
        filtering = new FilteringBehaviour(this, new ExtruderFilterSlotPositioning())
                .forRecipes();
        behaviours.add(filtering);

        extrudingBehaviour = new ExtrudingBehaviour(this);
        behaviours.add(extrudingBehaviour);

    }
    public ExtrudingBehaviour getExtrudingBehaviour() {
        return extrudingBehaviour;
    }
    @Override
    public void onExtrudingCompleted() {
        //extrudingBehaviour.resetBonks();
    }

    @Override
    public float getKineticSpeed() {
        return getSpeed();
    }
    protected <C extends Container> boolean matchExtrudingRecipe(ExtrudingRecipe recipe) {
        if (recipe == null)
            return false;
        return ExtrudingRecipe.match(this, recipe);
    }
    @Override
    public boolean tryProcess(boolean simulate) {
        Optional<ExtrudingRecipe> recipe = getRecipe();

        if(!recipe.isPresent())
           return false;


        if(outputInv.getStackInSlot(0).getCount() == outputInv.getStackInSlot(0).getMaxStackSize()){
            return false;
        }
        if(!outputInv.getStackInSlot(0).isEmpty() && !outputInv.getStackInSlot(0).is(recipe.get().getResult().getStack().getItem())){
            return false;
        }
        if(simulate)
            return true;

        int requiredBonks = recipe.get().getRequiredBonks();
        int currentBonks = extrudingBehaviour.addBonk();
        if(currentBonks < requiredBonks){

            setChanged();
            return true;
        }
        extrudingBehaviour.resetBonks();
        ItemStack output = recipe.get().getResult().rollOutput();
        if(outputInv.getStackInSlot(0).isEmpty()){

            outputInv.setStackInSlot(0, output);
        } else if(outputInv.getStackInSlot(0).is(recipe.get().getResult().getStack().getItem())) {
            outputInv.getStackInSlot(0).grow(output.getCount());
        }
        return true;
    }

    public Optional<ExtrudingRecipe> getRecipe() {


        //return ModRecipes.findExtruding(getItemIngredients(),getFluidIngredients(),getCatalystItem(), level);
        return ModRecipes.findExtruding(this, level);
    }

        @Override
    public void invalidate() {
        super.invalidate();
        capability.invalidate();
    }


    public boolean hasIngredient(FluidIngredient fluidIngredient){
        Block leftBlock = getLeftBlock();
        boolean found = false;
        if((leftBlock instanceof LiquidBlock)
            && fluidIngredient.getMatchingFluidStacks().contains(new FluidStack(((LiquidBlock) leftBlock).getFluid(),1000)
        ))
            found = true;
        Block rightBlock = getLeftBlock();
        if((rightBlock instanceof LiquidBlock)
                && fluidIngredient.getMatchingFluidStacks().contains(new FluidStack(((LiquidBlock) rightBlock).getFluid(),1000)
        ))
            found = true;
        return found;
    }
    public boolean hasIngredient(Ingredient ingredient){
        Block leftBlock = getLeftBlock();
        boolean found = false;
        if(!(leftBlock instanceof LiquidBlock)
                && ingredient.test(new ItemStack(leftBlock.asItem())
        ))
            found = true;
        Block rightBlock = getLeftBlock();
        if(!(rightBlock instanceof LiquidBlock)
                && ingredient.test(new ItemStack(rightBlock.asItem())
        ))
            found = true;
        return found;
    }

    public FilteringBehaviour getFilter() {
        return filtering;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Timer", timer);
        compound.put("OutputInventory", outputInv.serializeNBT());
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean addToGoggleTooltip = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        int currentBonks = extrudingBehaviour.getBonks();
        Lang.translate("create_mechanical_extruder.goggles.bonks",currentBonks)
                .forGoggles(tooltip, 1);
        return true;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        timer = compound.getInt("Timer");
        outputInv.deserializeNBT(compound.getCompound("OutputInventory"));
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isItemHandlerCap(cap))
            return capability.cast();
        return super.getCapability(cap, side);
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
    private Block getBelowBlock(){
        BlockPos currentPos = this.getBlockPos();
        return this.level.getBlockState(currentPos.below()).getBlock();
    }
    public NonNullList<ItemStack> getItemStacks() {
        NonNullList<ItemStack> itemStacks = NonNullList.create();
        Block leftBlock = getLeftBlock();
        Block rightBlock = getRightBlock();

        if(!(leftBlock instanceof LiquidBlock) && !(leftBlock instanceof AirBlock)){
            itemStacks.add( new ItemStack(leftBlock.asItem()));
        }
        if(!(rightBlock instanceof LiquidBlock) && !(leftBlock instanceof AirBlock)){
            itemStacks.add( new ItemStack(rightBlock.asItem()));
        }
        return itemStacks;
    }
    public NonNullList<FluidStack> getFluidStacks() {
        NonNullList<FluidStack> fluidStacks = NonNullList.create();
        Block leftBlock = getLeftBlock();
        Block rightBlock = getRightBlock();

        if((leftBlock instanceof LiquidBlock) && !(leftBlock instanceof AirBlock)){
            fluidStacks.add( new FluidStack(((LiquidBlock) leftBlock).getFluid(),1000));
        }
        if((rightBlock instanceof LiquidBlock) && !(leftBlock instanceof AirBlock)){
            fluidStacks.add( new FluidStack(((LiquidBlock) rightBlock).getFluid(),1000));
        }
        return fluidStacks;
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

    public NonNullList<FluidIngredient> getFluidIngredients() {
        NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
        Block leftBlock = getLeftBlock();
        Block rightBlock = getRightBlock();

        if(leftBlock instanceof LiquidBlock){
            fluidIngredients.add( FluidIngredient.fromFluid(((LiquidBlock) leftBlock).getFluid(),1000 ));
        }
        if(rightBlock instanceof LiquidBlock){
            fluidIngredients.add( FluidIngredient.fromFluid(((LiquidBlock) rightBlock).getFluid(),1000 ));
        }

        return fluidIngredients;
    }

    public Item getCatalystItem() {
        Block below = getBelowBlock();
        return below.asItem();
    }

    public List<String> getAllIngredientsStringList() {
        List<String> list = new ArrayList<>();
        getItemIngredients().forEach(ingredient -> list.add((!ingredient.isEmpty()) ? ingredient.getItems()[0].getItem().toString() : ItemStack.EMPTY.toString()));
        getFluidIngredients().forEach(ingredient -> list.add(ingredient.getMatchingFluidStacks().get(0).getFluid().getFluidType().getDescriptionId()));
        Collections.sort(list);
        return list;
    }




    private Biome getBiome() {
        return this.getLevel().getBiome(this.getBlockPos()).value();
    }

    private class ExtruderInventoryHandler extends CombinedInvWrapper {

        public ExtruderInventoryHandler() {
            super(outputInv);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;
            return super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }


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
