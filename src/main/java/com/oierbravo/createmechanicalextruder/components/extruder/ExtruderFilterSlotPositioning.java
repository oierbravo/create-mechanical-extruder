package com.oierbravo.createmechanicalextruder.components.extruder;

import com.simibubi.create.foundation.tileEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExtruderFilterSlotPositioning extends ValueBoxTransform.Sided {

    @Override
    protected boolean isSideActive(BlockState state, Direction direction) {
        Direction facing = state.getValue(ExtruderBlock.HORIZONTAL_FACING);
        return facing == direction;
    }
    @Override
    protected Vec3 getSouthLocation() {
        return VecHelper.voxelSpace(8f, 14f,  16f);
    }
}
