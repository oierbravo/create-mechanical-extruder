package com.oierbravo.createmechanicalextruder.components.extruder;

import com.oierbravo.createmechanicalextruder.register.ModShapes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractExtruderBlock<EX extends BlockEntity> extends HorizontalKineticBlock implements IBE<EX> {
    public AbstractExtruderBlock(Properties properties) {
        super(properties);
    }
    protected abstract IItemHandlerModifiable getOutputInventory(EX extruder);
    protected abstract void sendDataInternal(EX extruder);
    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        withBlockEntityDo(level, pos, extruder -> {
            IItemHandlerModifiable inv = getOutputInventory(extruder);
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                player.getInventory()
                        .placeItemBackInInventory(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
            }

            extruder.setChanged();
            sendDataInternal(extruder);
        });

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide == null)
            prefferedSide = context.getHorizontalDirection();
        return defaultBlockState().setValue(HORIZONTAL_FACING, context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown() ? prefferedSide : prefferedSide.getOpposite());
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext
                && ((EntityCollisionContext) context).getEntity() instanceof Player)
            return ModShapes.EXTRUDER;


       return AllShapes.MECHANICAL_PROCESSOR_SHAPE;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING)
                .getOpposite();
    }



    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
            withBlockEntityDo(worldIn, pos, te -> {
                ItemHelper.dropContents(worldIn, pos, getOutputInventory(te));
            });

            worldIn.removeBlockEntity(pos);
        }
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
}
