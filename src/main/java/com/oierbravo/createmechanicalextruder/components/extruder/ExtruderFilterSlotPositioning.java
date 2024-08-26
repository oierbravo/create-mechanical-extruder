package com.oierbravo.createmechanicalextruder.components.extruder;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExtruderFilterSlotPositioning extends ValueBoxTransform {
    private Direction facing;
    private BlockState currentState;

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        float y = 16.1f;
        float z = 3.2f;
        return VecHelper.rotateCentered(VecHelper.voxelSpace(8, y, z), angleY(state), Direction.Axis.Y);
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        TransformStack.cast(ms)
                .rotateY(angleY(state))
                .rotateX(90);
    }
    protected float angleY(BlockState state) {
        float horizontalAngle = AngleHelper.horizontalAngle(state.getValue(ExtruderBlock.HORIZONTAL_FACING));
            horizontalAngle += 180;
        return horizontalAngle;
    }
    protected float angle(BlockState state) {
        float horizontalAngle = ModBlocks.MECHANICAL_EXTRUDER.has(state)
                ? AngleHelper.horizontalAngle(state.getValue(ExtruderBlock.HORIZONTAL_FACING))
                : 0;
        return horizontalAngle;
    }
}
