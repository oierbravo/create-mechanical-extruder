package com.oierbravo.createmechanicalextruder.components.extruder;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.components.press.PressingBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;

public class ExtruderTileEntity extends KineticTileEntity implements ExtrudingBehaviour.ExtrudingBehaviourSpecifics {
    public ItemStackHandler outputInv;
    public LazyOptional<IItemHandler> capability;
    public int timer;
    //private ExtruderRecipe lastRecipe;

    public ItemStackHandler meshInv;
    public ExtrudingBehaviour extudingBehaviour;

    public ExtruderTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        outputInv = new ItemStackHandler(9);
        capability = LazyOptional.of(ExtruderInventoryHandler::new);
    }
    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        extudingBehaviour = new ExtrudingBehaviour(this);
        behaviours.add(extudingBehaviour);

    }
    public ExtrudingBehaviour getExtrudinggBehaviour() {
        return extudingBehaviour;
    }
    @Override
    public void onExtrudingCompleted() {

    }

    @Override
    public float getKineticSpeed() {
        return getSpeed();
    }


    protected boolean isRunning() {
        return extudingBehaviour.running;
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        if (getSpeed() == 0)
            return;
        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        float pitch = Mth.clamp((Math.abs(getSpeed()) / 256f) + .45f, .85f, 1f);
        SoundScapes.play(SoundScapes.AmbienceGroup.MILLING, worldPosition, pitch);
    }*/

    @Override
    public void tick() {
        super.tick();

        if (getSpeed() == 0)
            return;
        for (int i = 0; i < outputInv.getSlots(); i++)
            if (outputInv.getStackInSlot(i)
                    .getCount() == outputInv.getSlotLimit(i))
                return;

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
                //spawnParticles();
                return;
            }
            if (timer <= 0)
                process();
            return;
        }

       // if (inputInv.getStackInSlot(0)
       //         .isEmpty())
        //    return;

        /*RecipeWrapper inventoryIn = new RecipeWrapper(inputAndMeshCombined);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<ExtruderRecipe> recipe = ModRecipeTypes.SIFTING.find(inventoryIn, level);
            if (!recipe.isPresent()) {
                timer = 100;
                sendData();
            } else {
                lastRecipe = recipe.get();
                timer = lastRecipe.getProcessingDuration();
                sendData();
            }
            return;
        }*/

        //timer = lastRecipe.getProcessingDuration();
        sendData();
    }
    @Override
    public void setRemoved() {
        super.setRemoved();
        capability.invalidate();
    }

    private void process() {

        //RecipeWrapper inventoryIn = new RecipeWrapper(inputAndMeshCombined);

        //if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            /*Optional<ExtruderRecipe> recipe = ModRecipeTypes.SIFTING.find(inventoryIn, level);
            if (!recipe.isPresent())
                return;
            lastRecipe = recipe.get();*/
        //}

        /*ItemStack stackInSlot = inputInv.getStackInSlot(0);
        stackInSlot.shrink(1);
        inputInv.setStackInSlot(0, stackInSlot);
        lastRecipe.rollResults()
                .forEach(stack -> ItemHandlerHelper.insertItemStacked(outputInv, stack, false));
        sendData();
        setChanged();*/
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
    private boolean canProcess(ItemStack stack) {

        ItemStackHandler tester = new ItemStackHandler(2);
        tester.setStackInSlot(0, stack);

        //if (lastRecipe != null && lastRecipe.matches(inventoryIn, level))
        //    return true;
        return true;
        //return ModRecipeTypes.SIFTING.find(inventoryIn, level)
        //        .isPresent();
    }
    private class ExtruderInventoryHandler extends CombinedInvWrapper {

        public ExtruderInventoryHandler() {
            super( outputInv);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;
            return canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return ItemStack.EMPTY;
        }


    }
}
