package com.oierbravo.createmechanicalextruder.components.extruder;

import com.oierbravo.createmechanicalextruder.register.ModShapes;
import com.oierbravo.createmechanicalextruder.register.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock ;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ExtruderBlock extends HorizontalKineticBlock implements IBE<ExtruderBlockEntity> {
    public ExtruderBlock(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        ItemStack handInStack = player.getItemInHand(handIn);

        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;
        if (!handInStack.isEmpty())
            return InteractionResult.PASS;

        withBlockEntityDo(worldIn, pos, extruder -> {
            IItemHandlerModifiable inv = extruder.outputInv;
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);

                player.getInventory()
                        .placeItemBackInInventory(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
            }



            extruder.setChanged();
            extruder.sendData();
        });

        return InteractionResult.SUCCESS;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide == null)
            prefferedSide = context.getHorizontalDirection();
        //if(prefferedSide == Direction.DOWN || prefferedSide == Direction.UP)
        //    prefferedSide = Direction.NORTH;
        //context.getHorizontalDirection()
        return defaultBlockState().setValue(HORIZONTAL_FACING, context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown() ? prefferedSide : prefferedSide.getOpposite());
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ModShapes.EXTRUDER;
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
                ItemHelper.dropContents(worldIn, pos, te.outputInv);
            });

            worldIn.removeBlockEntity(pos);
        }
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }



    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<ExtruderBlockEntity> getBlockEntityClass() {
        return ExtruderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ExtruderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.MECHANICAL_EXTRUDER.get();
    }
}
