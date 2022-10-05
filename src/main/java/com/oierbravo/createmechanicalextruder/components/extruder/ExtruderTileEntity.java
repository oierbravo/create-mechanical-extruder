package com.oierbravo.createmechanicalextruder.components.extruder;

import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ExtruderTileEntity extends KineticTileEntity implements ExtrudingBehaviour.ExtrudingBehaviourSpecifics {
    public ItemStackHandler outputInv;
    public LazyOptional<IItemHandler> capability;
    public int timer;
    //private ExtruderRecipe lastRecipe;

    public ExtrudingBehaviour extrudingBehaviour;

    public ExtruderTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        outputInv = new ItemStackHandler(1);
        capability = LazyOptional.of(ExtruderInventoryHandler::new);
    }
    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        extrudingBehaviour = new ExtrudingBehaviour(this);
        behaviours.add(extrudingBehaviour);

    }
    public ExtrudingBehaviour getExtrudingBehaviour() {
        return extrudingBehaviour;
    }
    @Override
    public void onExtrudingCompleted() {
        int a = 10;
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
        if(!outputInv.getStackInSlot(0).isEmpty() && !outputInv.getStackInSlot(0).is(recipe.get().getResultItem().getItem())){
            return false;
        }
        if(simulate)
            return true;
       if(outputInv.getStackInSlot(0).isEmpty()){
            outputInv.setStackInSlot(0, new ItemStack(recipe.get().getResultItem().getItem(),recipe.get().getResultItem().getCount()));
        } else if(outputInv.getStackInSlot(0).is(recipe.get().getResultItem().getItem())) {
            outputInv.getStackInSlot(0).grow(1);
        }
        return true;
    }

    public Optional<ExtrudingRecipe> getRecipe() {


        //return ModRecipes.findExtruding(getItemIngredients(),getFluidIngredients(),getCatalystItem(), level);
        return ModRecipes.findExtruding(this, level);
    }
    @Override
    public void setRemoved() {
        super.setRemoved();
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



    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Timer", timer);
        compound.put("OutputInventory", outputInv.serializeNBT());

        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        timer = compound.getInt("Timer");
        outputInv.deserializeNBT(compound.getCompound("OutputInventory"));
        super.read(compound, clientPacket);
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

    public Block getLeftBlock(){
        BlockPos currentPos = this.getBlockPos();
        int x = currentPos.getX();
        return this.level.getBlockState(new BlockPos(x-1,currentPos.getY(),currentPos.getZ())).getBlock();
    }
    public Block getRightBlock(){
        BlockPos currentPos = this.getBlockPos();
        int x = currentPos.getX();
        return this.level.getBlockState(new BlockPos(x+1,currentPos.getY(),currentPos.getZ())).getBlock();
    }
    public Block getBelowBlock(){
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

    public List<String> getAllIngredients() {
        List<String> list = new ArrayList<>();
        getItemIngredients().forEach(ingredient -> list.add((!ingredient.isEmpty()) ? ingredient.getItems()[0].getItem().toString() : ItemStack.EMPTY.toString()));
        getFluidIngredients().forEach(ingredient -> list.add(ingredient.getMatchingFluidStacks().get(0).getFluid().getRegistryName().toString()));
        Collections.sort(list);
        return list;
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
}
