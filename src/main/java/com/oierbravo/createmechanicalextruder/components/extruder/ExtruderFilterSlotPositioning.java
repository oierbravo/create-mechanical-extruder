package com.oierbravo.createmechanicalextruder.components.extruder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.createmechanicalextruder.components.extruder.andesite.ExtruderBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExtruderFilterSlotPositioning extends ValueBoxTransform {
    private Direction facing;
    private BlockState currentState;

    /*@Override
    public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
        float y = 16f;
        float z = 3f;
        return VecHelper.rotateCentered(VecHelper.voxelSpace(8, y, z), angleY(state), Direction.Axis.Y);
    }

    @Override
    public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
        TransformStack.of(ms)
                .rotateY(angleY(state))
                .rotateX(90);
    }*/
    protected float angleY(BlockState state) {
        float horizontalAngle = AngleHelper.horizontalAngle(state.getValue(ExtruderBlock.HORIZONTAL_FACING));
            horizontalAngle += 180;
        return horizontalAngle;
    }
    /*protected float angle(BlockState state) {
        float horizontalAngle = ModBlocks.MECHANICAL_EXTRUDER.has(state)
                ? AngleHelper.horizontalAngle(state.getValue(ExtruderBlock.HORIZONTAL_FACING))
                : 0;
        return horizontalAngle;
    }*/


    @Override
    public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
        float y = 16f;
        float z = 3f;
        return VecHelper.rotateCentered(VecHelper.voxelSpace(8, y, z), angleY(state), Direction.Axis.Y);
    }

    @Override
    public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
        Direction facing = state.getValue(ExtruderBlock.HORIZONTAL_FACING);
        float yRot = AngleHelper.horizontalAngle(facing) + 180;
        TransformStack.of(ms)
                .rotateYDegrees(yRot)
                .rotateXDegrees(90);
        /*TransformStack.of(ms)
                .rotateY(angleY(state))
                .rotateZ(90);*/
    }
}
